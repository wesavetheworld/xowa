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
package gplx.xowa.xtns.cites; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
import gplx.core.brys.*; import gplx.core.brys.fmtrs.*; import gplx.core.brys.args.*;
class Xoh_ref_list_fmtr implements gplx.core.brys.Bfr_arg {
	private Xowe_wiki wiki; private Ref_html_wtr_cfg cfg; private Ref_nde itm;
	private final    Bfr_arg__bry_fmtr fmtr = Bfr_arg_.New_bry_fmtr__null();
	public void Init(Xowe_wiki wiki, Ref_html_wtr_cfg cfg, Ref_nde itm) {
		this.wiki = wiki; this.cfg = cfg; this.itm = itm;
	}
	public Ref_nde IdentifyTxt() {
		if (HasTxt(itm)) return itm;
		int itm_related_len = itm.Related_len();
		for (int i = 0; i < itm_related_len; i++) {
			Ref_nde rel = itm.Related_get(i);
			if (HasTxt(rel)) return rel;
		}
		return itm; // no itm has text; TODO_OLD:WARN
	}
	private boolean HasTxt(Ref_nde v) {return v.Body() != null && v.Body().Root_src().length > 0;}
	public void Bfr_arg__add(Bry_bfr bfr) {
		int related_len = itm.Related_len();
		Bry_fmtr itm_fmtr = cfg.Grp_html_list();
		Fmt(itm_fmtr, wiki, bfr, itm);
		for (int i = 0; i < related_len; i++) {
			Ref_nde link_itm = itm.Related_get(i);
			if (link_itm.Nested()) continue;
			Fmt(itm_fmtr, wiki, bfr, link_itm);
		}
	}
	private void Fmt(Bry_fmtr itm_fmtr, Xowe_wiki wiki, Bry_bfr trg, Ref_nde itm) {
		int itm_idx_minor = itm.Idx_minor();
		if (itm_idx_minor < 0) return;	// HACK: <ref follow created a negative index; ignore these references for now; de.wikisource.org/wiki/Seite:Die Trunksucht.pdf/63; DATE:2013-06-22
		byte[] backlabel 
			= itm_idx_minor < cfg.Backlabels_len()
			? cfg.Backlabels()[itm.Idx_minor()]
			: wiki.Parser_mgr().Main().Parse_text_to_html(wiki.Parser_mgr().Ctx(), wiki.Msg_mgr().Val_by_key_args(Ref_html_wtr_cfg.Msg_backlabels_err, itm.Idx_minor()))
			;
		itm_fmtr.Bld_bfr_many(trg
			, fmtr.Set(cfg.Itm_id_key_one(), itm.Name(), itm.Idx_major(), itm.Idx_minor())
			, backlabel
			);
	}
}
