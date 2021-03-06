/*
XOWA: the XOWA Offline Wiki Application
Copyright (C) 2012 gnosygnu@gmail.com

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as
published by the Free Software Foundation, either version 3 of the
License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package gplx.xowa.wikis.dbs; import gplx.*; import gplx.xowa.*; import gplx.xowa.wikis.*;
import gplx.core.primitives.*; import gplx.core.brys.*; import gplx.core.flds.*; import gplx.core.envs.*;
import gplx.xowa.bldrs.cmds.ctgs.*; import gplx.xowa.wikis.ctgs.*; import gplx.core.encoders.*;
import gplx.xowa.wikis.nss.*;
import gplx.xowa.wikis.data.*; import gplx.xowa.wikis.data.tbls.*;
import gplx.xowa.wikis.tdbs.*; import gplx.xowa.wikis.tdbs.hives.*; import gplx.xowa.wikis.tdbs.xdats.*;
import gplx.xowa.wikis.pages.*;
import gplx.xowa.addons.wikis.searchs.specials.*;
import gplx.xowa.guis.views.*;
public class Xodb_load_mgr_txt implements Xodb_load_mgr {
	private final    Xob_xdat_file tmp_xdat_file = new Xob_xdat_file(); private final    Xob_xdat_itm tmp_xdat_itm = new Xob_xdat_itm(); 
	private final    Xowd_page_itm tmp_page = new Xowd_page_itm();
	private final    Object thread_lock = new Object();
	private Xowe_wiki wiki; private Xotdb_fsys_mgr fsys_mgr;
	public Xodb_load_mgr_txt(Xowe_wiki wiki) {
		this.wiki = wiki;
		this.fsys_mgr = wiki.Tdb_fsys_mgr();
	}
	public void Load_init			(Xowe_wiki wiki) {}
	public void Load_page(Xowd_page_itm rv, Xow_ns ns) {Load_page(rv, rv.Text_db_id(), rv.Tdb_row_idx(), ns, false, tmp_xdat_file, tmp_xdat_itm);}
	public void Load_page(Xowd_page_itm rv, int txt_fil_idx, int txt_row_idx, Xow_ns ns, boolean timestamp_enabled, Xob_xdat_file xdat_file, Xob_xdat_itm xdat_itm) {
		Io_url file = fsys_mgr.Url_ns_fil(Xotdb_dir_info_.Tid_page, ns.Id(), txt_fil_idx);
		byte[] bry = gplx.core.ios.streams.Io_stream_rdr_.Load_all(file); int bry_len = bry.length;
		xdat_file.Clear().Parse(bry, bry_len, file).GetAt(xdat_itm, txt_row_idx);
		Load_page_parse(rv, bry, bry_len, xdat_itm.Itm_bgn(), xdat_itm.Itm_end(), timestamp_enabled);
	}
	public boolean Load_by_ttl(Xowd_page_itm rv, Xow_ns ns, byte[] ttl) {	// NOTE: ttl must be correct case; EX: "Example title"
		if (!Env_.Mode_testing() && wiki.Init_needed()) wiki.Init_assert();	// NOTE: need to call assert as wiki_finder (and possibly elsewhere) may call load on commons_wiki without ever asserting; DATE:2013-03-19
		if (!Load_xdat_itm(tmp_xdat_itm, ns, Xotdb_dir_info_.Tid_ttl, ttl, Xotdb_page_itm_.Txt_ttl_pos, Byte_ascii.Tab, true)) return false;
		Xotdb_page_itm_.Txt_ttl_load(rv, tmp_xdat_itm.Itm_bry());
		return Bry_.Eq(rv.Ttl_page_db(), ttl);
	}
	public void Load_by_ttls(Cancelable cancelable, Ordered_hash rv, boolean fill_idx_fields_only, int bgn, int end) {// NOTE: Load_by_ttls just a wrapper around Load_by_ttl; for xdat, Load_by_ttl is fast enough
		for (int i = bgn; i < end; i++) {
			if (cancelable.Canceled()) return;
			Xowd_page_itm page = (Xowd_page_itm)rv.Get_at(i);
			Load_by_ttl(page, page.Ns(), page.Ttl_page_db());
		}
	}
	public void Load_by_ids(Cancelable cancelable, List_adp list, int bgn, int end) {
		int prv_fil_idx = -1;
		byte[] id_bry = new byte[5];
		int len = end - bgn;
		Gfo_usr_dlg_fmt msg_wtr = Gfo_usr_dlg_fmt.fmt_(GRP_KEY, "search2_ids", "resolving ids: ~{0} of ~{1} (~{2})", len, 10f);
		for (int i = 0; i < len; i++) {
			if (cancelable.Canceled()) return;
			Xowd_page_itm itm = (Xowd_page_itm)list.Get_at(i + bgn);
			Base85_.Set_bry(itm.Id(), id_bry, 0, 5);
			int cur_fil_idx = this.Find_file_idx_by_site(Xotdb_dir_info_.Tid_id, id_bry);
			if (cur_fil_idx != prv_fil_idx) {
				if (!this.Load_xdat_file(cancelable, tmp_xdat_file, Xotdb_dir_info_.Tid_id, cur_fil_idx)) continue; // file not found; ignore
				prv_fil_idx = cur_fil_idx;
			}
			if (!this.Load_by_id(tmp_page, tmp_xdat_file, id_bry)) continue; // id not found in file; ignore	
			itm.Ns_id_(tmp_page.Ns_id()).Ttl_page_db_(tmp_page.Ttl_page_db());
			msg_wtr.Write_prog_cur(i, wiki.Appe().Usr_dlg());
		}
	}
	public void Load_search(Cancelable cancelable, List_adp rv, byte[] search, int results_max) {
		Xow_ns ns = wiki.Ns_mgr().Ns_main();
		int search_len = search.length;
		byte match_tid = Srch_special_page.Match_tid_all;
		if (search_len > 0 && search[search_len - 1] == Byte_ascii.Star) {
			search = Bry_.Mid(search, 0, search_len - 1);
			match_tid = Srch_special_page.Match_tid_bgn;
		}
		int bgn_idx = this.Find_file_idx_by_ns(Xotdb_dir_info_.Tid_search_ttl, ns, search);
		if (bgn_idx == Xodb_save_mgr_txt.File_idx_unknown) return;
		if (match_tid == Srch_special_page.Match_tid_all) {
			if (!this.Load_xdat_file(cancelable, tmp_xdat_file, Xotdb_dir_info_.Tid_search_ttl, ns, bgn_idx)) return;			
			tmp_xdat_file.Find(tmp_xdat_itm, search, 0, Byte_ascii.Pipe, true);
			if (tmp_xdat_itm.Missing()) return;
			Find_ttls__add_itms(rv, tmp_xdat_file, tmp_xdat_itm);
		}
		else {
			byte[] end_ttl = Bry_.Increment_last(Bry_.Copy(search));
			int end_idx = this.Find_file_idx_by_ns(Xotdb_dir_info_.Tid_search_ttl, ns, end_ttl);
			for (int i = bgn_idx; i <= end_idx; i++) {
				if (cancelable.Canceled()) return;
				this.Load_xdat_file(cancelable, tmp_xdat_file, Xotdb_dir_info_.Tid_search_ttl, ns, i);
				if (cancelable.Canceled()) return;
				int itm_bgn_idx = 0;
				if (i == bgn_idx) {
					tmp_xdat_file.Find(tmp_xdat_itm, search, 0, Byte_ascii.Pipe, false);
					itm_bgn_idx = tmp_xdat_itm.Itm_idx();
				}
				int itm_end_idx = tmp_xdat_file.Count();
				if (i == end_idx) {
					tmp_xdat_file.Find(tmp_xdat_itm, end_ttl, 0, Byte_ascii.Pipe, false);
					itm_end_idx = tmp_xdat_itm.Itm_idx();
				}
				for (int j = itm_bgn_idx; j < itm_end_idx; j++) {
					tmp_xdat_file.GetAt(tmp_xdat_itm, j);
					Find_ttls__add_itms(rv, tmp_xdat_file, tmp_xdat_itm);
				}
			}
		}
	}
	private void Find_ttls__add_itms(List_adp rv, Xob_xdat_file rdr, Xob_xdat_itm xdat_itm) {
		byte[] raw = rdr.Src();
		int itm_bgn = xdat_itm.Itm_bgn(), itm_end = xdat_itm.Itm_end();
		int pos = Bry_find_.Find_fwd(raw, Byte_ascii.Pipe, itm_bgn, raw.length);
		if (pos == Bry_find_.Not_found) throw wiki.Appe().Usr_dlg().Fail_many(GRP_KEY, "invalid_search_file", "search file is invalid");
		pos += Int_.Const_dlm_len;	// pipe
		
		while (pos < itm_end) {
			int page_id 	= Base85_.To_int_by_bry(raw, pos, pos + 4);
			pos += 6;	// 5 + 1 for semic;
			int page_len 	= Base85_.To_int_by_bry(raw, pos, pos + 4);
			rv.Add(Xowd_page_itm.new_srch(page_id, page_len));
			pos += 6;	// 5 + 1 for pipe
//				if (match.Itms_len() == max_results) break;
		}
	}
	public boolean Load_ctg_v2(Xoctg_data_ctg ctg, byte[] name) {
		boolean rv = Load_xdat_itm(tmp_xdat_itm, Xotdb_dir_info_.Tid_category2_link, name, true); if (!rv) return false;
		byte[] src = tmp_xdat_itm.Itm_bry();
		Load_ctg_v2_data(wiki.Appe().Usr_dlg(), wiki.Appe().Utl_fld_rdr(), ctg, name, src, tmp_ctg_grp_lens);
		Load_ctg_v2_main(ctg, name);
		return true;
	}	int[] tmp_ctg_grp_lens = new int[3];
	private void Load_ctg_v2_data(Gfo_usr_dlg usr_dlg, Gfo_fld_rdr fld_rdr, Xoctg_data_ctg ctg, byte[] ctg_name, byte[] src, int[] tmp_grp_lens) {	// Name|subc_len|file_len|page_len|subc_bfr|file_bfr|page_bfr
		fld_rdr.Data_(src); fld_rdr.Read_bry_escape(); // 1st field is name; skip;
		Load_ctg_v2_data_lens(fld_rdr, tmp_grp_lens);
		Load_ctg_v2_data_mgrs(usr_dlg, ctg_name, src, fld_rdr.Pos(), tmp_grp_lens, ctg.Grp_mgrs());
	}
	private void Load_ctg_v2_data_lens(Gfo_fld_rdr fld_rdr, int[] grp_lens) {
		for (int i = 0; i < 3; i++)
			grp_lens[i] = fld_rdr.Read_int_base85_len5();
	}
	private void Load_ctg_v2_data_mgrs(Gfo_usr_dlg usr_dlg, byte[] ctg_name, byte[] src, int bgn, int[] grp_lens, Xoctg_idx_mgr[] grp_mgrs) {
		for (int i = 0; i < 3; i++) {
			int grp_len = grp_lens[i]; if (grp_len == 0) continue;
			int end = bgn + grp_len;
			Xoctg_idx_mgr grp_mgr = new Xoctg_idx_mgr();
			byte[] grp_src = Bry_.Mid(src, bgn, end);
			grp_mgr.Src_(grp_src);
			grp_mgr.Index(usr_dlg, ctg_name, grp_src);
			grp_mgrs[i] = grp_mgr;
			bgn = end;
		}
	}
	private void Load_ctg_v2_main(Xoctg_data_ctg rv, byte[] name) {
		if (!Load_xdat_itm(tmp_xdat_itm, Xotdb_dir_info_.Tid_category2_main, name, true)) return;
		if (tmp_xdat_itm.Missing()) return;
		byte[] bry = tmp_xdat_itm.Itm_bry();
		int bgn = name.length + 1;
		boolean hidden			= bry[bgn] == Byte_ascii.Ltr_y;
		int count_subcs		= Base85_.To_int_by_bry(bry, bgn +  2, bgn +  6);
		int count_files		= Base85_.To_int_by_bry(bry, bgn +  8, bgn + 12);
		int count_pages		= Base85_.To_int_by_bry(bry, bgn + 14, bgn + 18);
		rv.Hidden_(hidden);
		for (byte i = 0; i < Xoa_ctg_mgr.Tid__max; i++) {
			Xoctg_idx_mgr idx_mgr = rv.Grp_by_tid(i);
			if (idx_mgr == null) continue;
			int count = 0;
			switch (i) {
				case Xoa_ctg_mgr.Tid_subc:	count = count_subcs; break;
				case Xoa_ctg_mgr.Tid_file:	count = count_files; break;
				case Xoa_ctg_mgr.Tid_page:	count = count_pages; break;
				default:						throw Err_.new_unhandled(i);
			}
			idx_mgr.Total_(count);
		}
	}
	public boolean Load_by_id(Xowd_page_itm page, int id)			{Base85_.Set_bry(id, tmp_id_bry, 0, 5); return Load_by_id(page, tmp_id_bry);} private byte[] tmp_id_bry = new byte[5];
	boolean Load_by_id(Xowd_page_itm page, byte[] id_bry)	{
		if (!Load_xdat_itm(tmp_xdat_itm, Xotdb_dir_info_.Tid_id, id_bry, true)) return false;;
		Xotdb_page_itm_.Txt_id_load(page, tmp_xdat_itm.Itm_bry());
		return true;
	}
	boolean Load_by_id(Xowd_page_itm page, Xob_xdat_file xdat_file, byte[] id_bry) {
		xdat_file.Find(tmp_xdat_itm, id_bry, 0, Byte_ascii.Pipe, true);
		if (tmp_xdat_itm.Missing()) return false;
		Xotdb_page_itm_.Txt_id_load(page, tmp_xdat_itm.Itm_bry());
		return true;
	}
	private boolean Load_xdat_itm(Xob_xdat_itm xdat_itm, byte regy_tid, byte[] key, boolean exact) {return Load_xdat_itm(xdat_itm, null, regy_tid, key, 0, Byte_ascii.Pipe, exact);}
	private boolean Load_xdat_itm(Xob_xdat_itm xdat_itm, Xow_ns ns, byte regy_tid, byte[] key, int parse_bgn, byte parse_dlm, boolean exact) {
		// get regy
		Xowd_regy_mgr regy = null;
		if (ns == null)
			regy = Get_regy_by_site(regy_tid);
		else {
			regy = Get_regy_by_ns(ns);
			if (regy == null) return false;
		}
		// find file
		int fil_idx = regy.Files_find(key);
		if (fil_idx == Xowd_regy_mgr.Regy_null) return false;	// NOTE: must check for -1, not 0; else defect in which entries in file 0 are ignored; DATE:2013-04-11
		// load file
		Io_url fil = ns == null ? fsys_mgr.Url_site_fil(regy_tid, fil_idx) : fsys_mgr.Url_ns_fil(regy_tid, ns.Id(), fil_idx);
		Load_xdat_file(Cancelable_.Never, tmp_xdat_file, fil);
		// find itm by key
		tmp_xdat_file.Find(xdat_itm, key, parse_bgn, parse_dlm, exact);
		return !xdat_itm.Missing();
	}	private final    Int_obj_ref tmp_len = Int_obj_ref.New_zero();
	public boolean Load_xdat_file(Cancelable cancelable, Xob_xdat_file xdat_file, byte regy_tid, int fil_idx) {return Load_xdat_file(cancelable, xdat_file, regy_tid, null, fil_idx);}
	boolean Load_xdat_file(Cancelable cancelable, Xob_xdat_file xdat_file, byte regy_tid, Xow_ns ns, int fil_idx) {
		Io_url fil = ns == null ? fsys_mgr.Url_site_fil(regy_tid, fil_idx) : fsys_mgr.Url_ns_fil(regy_tid, ns.Id(), fil_idx);
		return Load_xdat_file(cancelable, xdat_file, fil);
	}
	public boolean Load_xdat_file(Cancelable cancelable, Xob_xdat_file xdat_file, Io_url url) {
		boolean rv = false;
		synchronized (thread_lock) {
			if (cancelable.Canceled()) return false;
			Bry_bfr tmp_bfr = wiki.Utl__bfr_mkr().Get_m001();
			byte[] tmp_bry = tmp_bfr.Bfr();
			if (cancelable.Canceled()) return false;
			tmp_bry = Io_mgr.Instance.LoadFilBry_reuse(url, tmp_bry, tmp_len);
			if (cancelable.Canceled()) return false;
			if (tmp_bry.length == 0)
				wiki.Appe().Usr_dlg().Warn_many("", "file.empty", "hive file is empty: ~{0}", url.Raw());
			else {
				int src_len = tmp_len.Val();
				xdat_file.Clear().Parse(tmp_bry, src_len, url);
				xdat_file.Src_len_(src_len);
				rv = true;
			}
			if (cancelable.Canceled()) return false;
			tmp_bfr.Clear_and_rls();
		}
		return rv;
	}
	int Find_file_idx_by_ns(byte regy_tid, Xow_ns ns, byte[] key) {
		Xowd_regy_mgr regy = new Xowd_regy_mgr(fsys_mgr.Url_ns_reg(ns.Num_str(), regy_tid));
		return regy.Files_find(key);
	}
	public int Find_file_idx_by_site(byte regy_tid, byte[] key) {
		Xowd_regy_mgr regy = site_regys[regy_tid];
		if (regy == null) {
			regy = new Xowd_regy_mgr(fsys_mgr.Url_site_reg(regy_tid));
			site_regys[regy_tid] = regy;
		}
		return regy.Files_find(key);
	}
	public void Clear() {
		int len = ns_regys.length;
		for (int i = 0; i < len; i++)
			ns_regys[i] = null;
		len = site_regys.length;
		for (int i = 0; i < len; i++)
			site_regys[i] = null;
	}
	public byte[] Find_random_ttl(Xow_ns ns) {
		Xowd_regy_mgr regy_mgr = this.Get_regy_by_ns(ns);
		Int_obj_ref count = Int_obj_ref.New_zero();
		Xob_random_itm[] files = Build_random_itms(regy_mgr, count);
		int random_idx = RandomAdp_.new_().Next(count.Val() - 1);	// get a random idx; -1 since count is super 1 (EX: count of 1 mil; random_idx of 0 - 999,999)
		int file_idx = CompareAble_.FindSlot(Xob_random_itm_comparer.Instance, files, new Xob_random_itm(-1, random_idx, -1));
		Io_url file_url = fsys_mgr.Url_ns_fil(Xotdb_dir_info_.Tid_ttl, ns.Id(), file_idx);
		Load_xdat_file(Cancelable_.Never, tmp_xdat_file, file_url);
		Xob_random_itm file = files[file_idx];
		tmp_xdat_file.GetAt(tmp_xdat_itm, random_idx - file.Bgn());	// get nth row; EX: random_idx=120; .Bgn=103 -> get 17th
		Xowd_page_itm page = Xotdb_page_itm_.Txt_ttl_load(tmp_xdat_itm.Itm_bry());
		return page.Ttl_page_db();
	}
	private static Xob_random_itm[] Build_random_itms(Xowd_regy_mgr mgr, Int_obj_ref count) {
		// convert regy to list of random_itms (similar to regy_itms, but has integer bgn / end; EX: [0]:0,50; [1]:51-102; [2]:103-130)
		Xowd_hive_regy_itm[] files_ary = mgr.Files_ary();
		int len = files_ary.length;
		Xob_random_itm[] rv = new Xob_random_itm[len];
		int tmp_count = 0;
		for (int i = 0; i < len; i++) {
			Xowd_hive_regy_itm file = files_ary[i];
			rv[i] = new Xob_random_itm(i, tmp_count, file.Count());
			tmp_count += file.Count();
		}
		count.Val_(tmp_count);
		return rv;
	}
	public boolean Load_ctg_v1(Xoctg_view_ctg view_ctg, byte[] ctg_ttl) {return Load_ctg_v1_wkr(view_ctg, ctg_ttl, null);}
	private boolean Load_ctg_v1_wkr(Xoctg_view_ctg view_ctg, byte[] ctg_ttl, Int_obj_ref count_only) {
		Xowd_regy_mgr ctg_regy = Get_regy_by_site(Xotdb_dir_info_.Tid_category);
		int fil_idx = ctg_regy.Files_find(ctg_ttl);
		if (fil_idx == Xowd_regy_mgr.Regy_null) return false;	// NOTE: must check for -1, not 0; else defect in which entries in file 0 are ignored; DATE:2013-04-11
		Io_url fil = fsys_mgr.Url_site_fil(Xotdb_dir_info_.Tid_category, fil_idx);
		Load_xdat_file(Cancelable_.Never, tmp_xdat_file, fil);
		tmp_xdat_file.Find(tmp_xdat_itm, ctg_ttl, 0, Byte_ascii.Pipe, true);
		if (tmp_xdat_itm.Missing()) return false;
		List_adp ctgs = List_adp_.New();
		Load_ctg_v1_parse(ctgs, wiki.Appe().Usr_dlg(), tmp_xdat_itm.Itm_bry());
		ctgs.Sort_by(Xowd_page_itm_sorter.IdAsc);
		this.Load_by_ids(Cancelable_.Never, ctgs, 0, ctgs.Count());
		ctgs.Sort_by(Xowd_page_itm_sorter.Ns_id_TtlAsc);

		int ctgs_len = ctgs.Count(); if (ctgs_len == 0) return false;
		if (count_only != null) {	// Ctg_count specificed
			count_only.Val_(ctgs_len);
			return true;
		}
		for (int i = 0; i < ctgs_len; i++) {
			Xowd_page_itm itm = (Xowd_page_itm)ctgs.Get_at(i);
			byte itm_tid = Load_ctg_v1_tid(itm.Ns_id());
			Xoctg_view_itm sub = new Xoctg_view_itm();
			sub.Set__page(itm_tid, itm.Id());
			sub.Set__ttl__sortkey(Xoa_ttl.Parse(wiki, itm.Ns_id(), itm.Ttl_page_db()), itm.Ttl_page_db());
			view_ctg.Grp_by_tid(itm_tid).Itms_add(sub);				
		}
		for (byte i = 0; i < Xoa_ctg_mgr.Tid__max; i++) {
			Xoctg_view_grp grp = view_ctg.Grp_by_tid(i);
			grp.Itms_make();
			grp.Total_(grp.Itms().length);
		}
		return true;
	}
	private static void Load_ctg_v1_parse(List_adp rv, Gfo_usr_dlg usr_dlg, byte[] ary) {
		int aryLen = ary.length;
		int pos = Bry_find_.Find_fwd(ary, Byte_ascii.Pipe, 0, aryLen);
		int rowCount = (aryLen - pos + 1) / (Base85_.Len_int + gplx.xowa.apps.progs.Xoa_prog_mgr.Len_dlm_fld);
		rv.Clear();
		boolean garbage = false;
		for (int i = 0; i < rowCount; i++) {
			Xowd_page_itm row = new Xowd_page_itm();
			rv.Add(row);
			if (garbage) continue;
			int bgn = pos + 1 + (i * (Base85_.Len_int + gplx.xowa.apps.progs.Xoa_prog_mgr.Len_dlm_fld));
			try {
				int id = Base85_.To_int_by_bry(ary, bgn, bgn + Base85_.Len_int - 1);
				if (id < 0) throw Err_.new_wo_type("invalid id", "id", id);
				row.Id_(id);
			}
			catch (Exception e) {
				row.Id_(0);
				usr_dlg.Warn_many("xowa.ttl_request", "parse", "failed to parse row: row=~{0} err=~{1}", String_.new_u8(ary), Err_.Message_lang(e));
				garbage = true;
				continue;	// NOTE: must break b/c rest of ids are garbage
			}
		}
	}
	public static byte Load_ctg_v1_tid(int ns_id) {
		switch (ns_id) {
			case Xow_ns_.Tid__category: return Xoa_ctg_mgr.Tid_subc;
			case Xow_ns_.Tid__file:		return Xoa_ctg_mgr.Tid_file;
			default: 					return Xoa_ctg_mgr.Tid_page;
		}		
	}
	public static boolean Load_page_or_false(Xowd_page_itm page, Xob_xdat_itm xdat, int ns_id) {
		byte[] src = xdat.Src(); int itm_end = xdat.Itm_end();
		int bgn = xdat.Itm_bgn();
		int timestamp	= Base85_.To_int_by_bry(src, bgn +  6		, bgn +  10);
		int ttl_end = Bry_find_.Find_fwd(src, Xotdb_page_itm_.Txt_page_dlm, bgn + 12, itm_end);
		if (ttl_end == -1) return false;
		byte[] ttl		= Bry_.Mid				(src, bgn + 12		, ttl_end);
		byte[] text		= Bry_.Mid				(src, ttl_end + 1	, itm_end - 1);
		page.Init_by_tdb(-1, -1, xdat.Itm_idx(), Bool_.N, text.length, ns_id, ttl);
		page.Modified_on_(Int_flag_bldr_.To_date_short(timestamp));
		page.Text_(text);
		return true;
	}
	private void Load_page_parse(Xowd_page_itm page, byte[] src, int src_len, int row_bgn, int row_end, boolean timestamp_enabled) { // \n\tdate5\tpage_title\tpage_text
		int timestamp_bgn = row_bgn + 5 + 1;
		int timestamp_end = timestamp_bgn + 5;
		if (timestamp_enabled) {
			int timestamp = Base85_.To_int_by_bry(src, timestamp_bgn, timestamp_end - 1);
			page.Modified_on_(Int_flag_bldr_.To_date_short(timestamp));
		}
		int name_bgn = timestamp_end + 1;
		int name_end = Bry_find_.Find_fwd(src, Xotdb_page_itm_.Txt_page_dlm, name_bgn, src_len);			
		page.Text_(Bry_.Mid(src, name_end + 1, row_end - 1));	// +1 to skip dlm
	}
	Xowd_regy_mgr Get_regy_by_site(byte regy_tid) {
		Xowd_regy_mgr rv = site_regys[regy_tid];
		if (rv == null) {
			rv = new Xowd_regy_mgr(fsys_mgr.Url_site_reg(regy_tid));
			site_regys[regy_tid] = rv;
		}
		return rv;
	}	private Xowd_regy_mgr[] site_regys = new Xowd_regy_mgr[Xotdb_dir_info_.Regy_tid_max]; 
	Xowd_regy_mgr Get_regy_by_ns(Xow_ns ns) {
		int ns_ord = ns.Ord();
		Xowd_regy_mgr rv = ns_regys[ns_ord];
		if (rv == null) {
			Io_url file = fsys_mgr.Url_ns_reg(ns.Num_str(), Xotdb_dir_info_.Tid_ttl);
			if (!Io_mgr.Instance.ExistsFil(file)) return null;
			rv = new Xowd_regy_mgr(file);
			ns_regys[ns_ord] = rv;
		}
		return rv;
	}	private Xowd_regy_mgr[] ns_regys = new Xowd_regy_mgr[Xow_ns_mgr_.Ordinal_max];
	private Xowd_page_itm tmp_rslt_nxt = new Xowd_page_itm(), tmp_rslt_prv = new Xowd_page_itm(); private Int_obj_ref tmp_rslt_count = Int_obj_ref.New_zero();
	public void Load_ttls_for_search_suggest(Cancelable cancelable, List_adp rslt_list, Xow_ns ns, byte[] key, int max_results, int min_page_len, int browse_len, boolean include_redirects, boolean fetch_prv_item) {
		this.Load_ttls_for_all_pages(cancelable, rslt_list, tmp_rslt_nxt, tmp_rslt_prv, tmp_rslt_count, ns, key, max_results, min_page_len, browse_len, include_redirects, fetch_prv_item);
	}
	public void Load_ttls_for_all_pages(Cancelable cancelable, List_adp rslt_list, Xowd_page_itm rslt_nxt, Xowd_page_itm rslt_prv, Int_obj_ref rslt_count, Xow_ns ns, byte[] key, int max_results, int min_page_len, int browse_len, boolean include_redirects, boolean fetch_prv_item) {
		byte dir_tid = Xotdb_dir_info_.Tid_ttl;
		Xob_xdat_file cur_xdat_file = new Xob_xdat_file();
		Xob_xdat_itm cur_xdat_itm = new Xob_xdat_itm();
		Xowd_regy_mgr regy = new Xowd_regy_mgr(fsys_mgr.Url_ns_reg(ns.Num_str(), dir_tid));
		int fil_idx = regy.Files_find(key); if (fil_idx == Xowd_regy_mgr.Regy_null) return;
		if (!this.Load_xdat_file(Cancelable_.Never, cur_xdat_file, dir_tid, ns, fil_idx)) return;
		cur_xdat_file.Find(cur_xdat_itm, key, Xotdb_page_itm_.Txt_ttl_pos, Byte_ascii.Tab, false);
		int itm_idx = cur_xdat_itm.Itm_idx();
		if (itm_idx == -1) itm_idx = 0;	// nothing found; return;
		Special_allpages_query_fwd(rslt_list, rslt_nxt, rslt_count	, dir_tid, ns, include_redirects, browse_len, fil_idx, itm_idx    , cur_xdat_file, cur_xdat_itm, regy);
		Special_allpages_query_bwd(rslt_list, rslt_prv				, dir_tid, ns, include_redirects, browse_len, fil_idx, itm_idx - 1, cur_xdat_file, cur_xdat_itm);
	}
	private void Special_allpages_query_fwd(List_adp rslt_list, Xowd_page_itm rslt_nxt, Int_obj_ref rslt_count, byte dir_tid, Xow_ns ns, boolean include_redirects, int total, int fil_idx, int row_idx, Xob_xdat_file xdat_file, Xob_xdat_itm xdat_itm, Xowd_regy_mgr regy) {
		int count = 0; ++total;
		boolean loop = true;
		int regy_len = regy.Files_ary().length;
		int rslt_list_len = rslt_count.Val();
		Xowd_page_itm nxt_itm = null;
		while (loop) {
			if (fil_idx == regy_len) break;
			if (xdat_file == null) {
				xdat_file = new Xob_xdat_file();
				this.Load_xdat_file(Cancelable_.Never, xdat_file, dir_tid, ns, fil_idx);
				row_idx = 0;
			}
			int rows_len = xdat_file.Count();
			for (; row_idx < rows_len; row_idx++) {
				xdat_file.GetAt(xdat_itm, row_idx);
				Xowd_page_itm ttl_itm = Xotdb_page_itm_.Txt_ttl_load(Bry_.Mid(xdat_itm.Src(), xdat_itm.Itm_bgn(), xdat_itm.Itm_end()));
				if (!include_redirects && ttl_itm.Redirected()) continue;
				++count;
				nxt_itm = ttl_itm;
				if (count == total) {
					loop = false;
					break;
				}
				else {
					rslt_list.Add(ttl_itm);
					++rslt_list_len;
				}
			}
			xdat_file = null;
			++fil_idx;
		}
		rslt_count.Val_(rslt_list_len);
		if (rslt_nxt != null)
			rslt_nxt.Copy(nxt_itm);
	}
	private void Special_allpages_query_bwd(List_adp rslt_list, Xowd_page_itm rslt_prv, byte dir_tid, Xow_ns ns, boolean include_redirects, int total, int fil_idx, int row_idx, Xob_xdat_file xdat_file, Xob_xdat_itm xdat_itm) {
		if (row_idx < 0) {
			--fil_idx;
			row_idx = -1;
		}
		int count = 0;
		boolean loop = true;
		Xowd_page_itm prv_itm = null;
		while (loop) {
			if (fil_idx == -1) break;
			if (xdat_file == null) {
				xdat_file = new Xob_xdat_file();
				this.Load_xdat_file(Cancelable_.Never, xdat_file, dir_tid, ns, fil_idx);
				row_idx = -1;
			}
			if (row_idx == -1)
				row_idx = xdat_file.Count() - 1;
			for (; row_idx > -1; row_idx--) {
				xdat_file.GetAt(xdat_itm, row_idx);
				Xowd_page_itm ttl_itm = Xotdb_page_itm_.Txt_ttl_load(Bry_.Mid(xdat_itm.Src(), xdat_itm.Itm_bgn(), xdat_itm.Itm_end()));
				if (!include_redirects && ttl_itm.Redirected()) continue;
//				list.Add(ttl_itm);
				++count;
				prv_itm = ttl_itm;
				if (count == total) {					
					loop = false;
					break;
				}
				else {
//					rslt_list_ttls[rslt_list_len++] = ttl_itm;
				}
			}
			xdat_file = null;
			--fil_idx;
		}
		if (prv_itm == null && rslt_list.Count() > 0) {
			prv_itm = (Xowd_page_itm)rslt_list.Get_at(0);
		}
		if (rslt_prv != null)
			rslt_prv.Copy(prv_itm);
	}
	public byte[] Load_qid(byte[] wiki_alias, byte[] ns_num, byte[] ttl) {
		String xwiki_key = String_.new_a7(wiki_alias);
		if (qids_root == null)
			qids_root = wiki.Appe().Wiki_mgr().Wdata_mgr().Wdata_wiki().Tdb_fsys_mgr().Site_dir().GenSubDir_nest("data", "qid");
		Xob_xdat_itm qid_itm = Load_xdat_itm_by_dir(qids_root.GenSubDir_nest(xwiki_key, String_.new_a7(ns_num)), ttl); if (qid_itm == null) return null;
		return Bry_.Mid(qid_itm.Src(), qid_itm.Itm_bgn() + ttl.length + 1, qid_itm.Itm_end());	// extract qid; note that all itms have format of "ttl|qid"
	}	Io_url qids_root;
	public int Load_pid(byte[] lang_key, byte[] pid_name) {
		if (pids_root == null)
			pids_root = wiki.Appe().Wiki_mgr().Wdata_mgr().Wdata_wiki().Tdb_fsys_mgr().Site_dir().GenSubDir_nest("data", "pid");
		Xob_xdat_itm pid_itm = Load_xdat_itm_by_dir(pids_root.GenSubDir(String_.new_u8(lang_key)), pid_name); if (pid_itm == null) return gplx.xowa.xtns.wbases.Wdata_wiki_mgr.Pid_null;
		return Bry_.To_int_or(pid_itm.Src(), pid_itm.Itm_bgn() + pid_name.length + 1 + 1, pid_itm.Itm_end(), gplx.xowa.xtns.wbases.Wdata_wiki_mgr.Pid_null);	// extract pid; note that all itms have format of "ttl|pid"; +1=skip pipe; +1 skip p
	}	Io_url pids_root;
	public int Load_ctg_count(byte[] ttl) {return wiki.Db_mgr().Category_version() == Xoa_ctg_mgr.Version_1 ? Load_ctg_count_v1(ttl) : Load_ctg_count_v2(ttl);}
	int Load_ctg_count_v1(byte[] ttl) {
		Xoctg_view_ctg view_ctg = new Xoctg_view_ctg();
		Int_obj_ref count = Int_obj_ref.New_zero();
		Load_ctg_v1_wkr(view_ctg, ttl, count);
		return count.Val();
	}
	int Load_ctg_count_v2(byte[] ttl) {
		Xoctg_data_ctg data_ctg = new Xoctg_data_ctg(ttl);
		Load_ctg_v2(data_ctg, ttl);
		return data_ctg.Total_count();
	}
	Xob_xdat_itm Load_xdat_itm_by_dir(Io_url dir, byte[] ttl) {
		Xoa_hive_mgr hive_mgr = wiki.Appe().Hive_mgr();
		int fil_idx = hive_mgr.Find_fil(dir, ttl); if (fil_idx == Xowd_regy_mgr.Regy_null) return null; // sub_dir not found; EX: commonswiki if qid; fr if pid;
		Xob_xdat_file rdr = hive_mgr.Get_rdr(dir, Xotdb_dir_info_.Bry_xdat, fil_idx);
		rdr.Find(tmp_xdat_itm, ttl, 0, Byte_ascii.Pipe, true);
		return tmp_xdat_itm.Found_exact() ? tmp_xdat_itm : null;
	}
	public void Load_ctg_v2a(Xoctg_view_ctg rv, Xoctg_url url_ctg, byte[] ttl_bry, int limit, boolean app_is_cmd) {
		Xoctg_html_mgr ctg_mgr = wiki.Html_mgr().Ns_ctg();
		Xoctg_data_cache data_cache = ctg_mgr.Data_cache();
		Xoctg_data_ctg data_ctg = data_cache.Get_or_null(ttl_bry);
		if (data_ctg == null) {
			data_ctg = data_cache.Load_or_null(wiki, ttl_bry);
			if (data_ctg == null) return;
		}
		rv.Fill(url_ctg, data_ctg);
		ctg_mgr.Get_titles(wiki.Appe().Usr_dlg(), wiki, rv);
		rv.Num_(data_ctg);
	}
	public Xowd_page_itm[] Load_ctg_list(byte[][] ttls) {
		int len = ttls.length;
		Xowd_page_itm[] rv = new Xowd_page_itm[len];
		Xow_ns ns = wiki.Ns_mgr().Ns_category();
		
		Xoctg_data_ctg ctg_temp = new Xoctg_data_ctg(Bry_.Empty);
		for (int i = 0; i < len; i++) {
			byte[] ttl = Xoa_ttl.Replace_spaces(ttls[i]);	// NOTE: ctg_ttls has spaces since v1 rendered it literally;
			Xowd_page_itm page = new Xowd_page_itm();
			this.Load_by_ttl(page, ns, ttl);

			Load_ctg_v2_main(ctg_temp, page.Ttl_page_db());
			Xowd_category_itm ctg_itm = Xowd_category_itm.load_(page.Id(), page.Text_db_id(), ctg_temp.Hidden(), ctg_temp.Total_by_tid(Xoa_ctg_mgr.Tid_subc), ctg_temp.Total_by_tid(Xoa_ctg_mgr.Tid_file), ctg_temp.Total_by_tid(Xoa_ctg_mgr.Tid_page)); 
			page.Xtn_(ctg_itm);
			rv[i] = page;
		}
		return rv;
	}
	public Xodb_page_rdr Get_page_rdr(Xowe_wiki wiki) {return new Xodb_page_rdr__tdb(wiki);}
	static final String GRP_KEY = "xowa.wiki.db.load";
}
class Xob_random_itm {
	public int Idx() {return idx;} private int idx;
	public int Bgn() {return bgn;} private int bgn;
	public int End() {return bgn + len;}
	public int Len() {return len;} private int len;
	public Xob_random_itm(int idx, int bgn, int len) {this.idx = idx; this.bgn = bgn; this.len = len;}
}
class Xob_random_itm_comparer implements gplx.core.lists.ComparerAble {
	public int compare(Object lhsObj, Object rhsObj) {
		return Int_.Compare(((Xob_random_itm)lhsObj).End(), ((Xob_random_itm)rhsObj).End());
	}
	public static final    Xob_random_itm_comparer Instance = new Xob_random_itm_comparer(); Xob_random_itm_comparer() {}
}
