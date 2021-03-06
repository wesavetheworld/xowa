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
package gplx.xowa.htmls.core.wkrs.glys; import gplx.*; import gplx.xowa.*; import gplx.xowa.htmls.*; import gplx.xowa.htmls.core.*; import gplx.xowa.htmls.core.wkrs.*;
import gplx.core.brys.*; import gplx.core.brys.fmtrs.*;
import gplx.langs.htmls.*; import gplx.xowa.htmls.core.wkrs.bfr_args.*;
class Xoh_gly_grp_wtr implements Bfr_arg {
	private final    Bfr_arg_clearable[] arg_ary;
	private final    Bfr_arg__hatr_id ul_id = Bfr_arg__hatr_id.New_id("xogly_ul_");
	private final    Bfr_arg__hatr__style ul_style = new Bfr_arg__hatr__style(Gfh_atr_.Bry__style);
	private final    Bfr_arg__hatr__xogly ul_xogly = new Bfr_arg__hatr__xogly();
	private final    Bfr_arg__elem__capt li_capt = new Bfr_arg__elem__capt();
	private byte[] ul_cls, xtra_cls, xtra_atr_bry, ul_nl;
	private final    Xoh_gly_itm_list_wtr itm_list_wtr = new Xoh_gly_itm_list_wtr();
	public Xoh_gly_grp_wtr() {
		arg_ary = new Bfr_arg_clearable[] {ul_id, ul_xogly, li_capt};
	}
	public void Init(boolean mode_is_diff, int id, int xnde_w, int xnde_h, int xnde_per_row, byte[] cls, int ul_style_max_w, int ul_style_w
		, byte[] xtra_cls, byte[] xtra_style_bry, byte[] xtra_atr_bry, byte[] capt, Xoh_gly_itm_wtr[] ary) {
		this.Clear();
		if (!mode_is_diff)
			ul_id.Set(id); 
		ul_xogly.Set_args(xnde_w, xnde_h, xnde_per_row);
		this.ul_cls = cls;
		this.xtra_cls = xtra_cls == null ? Bry_.Empty : Bry_.Add(Byte_ascii.Space_bry, xtra_cls);
		this.xtra_atr_bry = xtra_atr_bry;
		this.ul_nl = ary.length == 0 ? Bry_.Empty : Byte_ascii.Nl_bry;	// TIDY: <ul></ul> should be on same line if 0 items
		li_capt.Capt_(capt);
		itm_list_wtr.Init(ary);
		ul_style.Set_args(ul_style_max_w, ul_style_w, xtra_style_bry);			
	}
	public void Clear() {
		for (Bfr_arg_clearable arg : arg_ary)
			arg.Bfr_arg__clear();
		ul_id.Bfr_arg__clear();
		ul_style.Clear();
		ul_cls = null;
	}
	public void Bfr_arg__clear() {this.Clear();}
	public boolean Bfr_arg__missing() {return false;}
	public void Bfr_arg__add(Bry_bfr bfr) {
		fmtr.Bld_bfr_many(bfr, ul_id, ul_xogly, ul_cls, xtra_cls, ul_style, xtra_atr_bry, li_capt, itm_list_wtr, ul_nl);
	}
	private static final    Bry_fmtr fmtr = Bry_fmtr.new_(String_.Concat_lines_nl_skip_last
	( "<ul~{id}~{xogly} class=\"gallery mw-gallery-~{cls}~{xtra_cls}\"~{style}~{xtra_atr}>~{capt}~{itms}~{ul_nl}</ul>"
	), "id", "xogly", "cls", "xtra_cls", "style", "xtra_atr", "capt", "itms", "ul_nl");
}
class Xoh_gly_itm_list_wtr implements Bfr_arg {
	private Xoh_gly_itm_wtr[] ary; private int ary_len;
	public void Init(Xoh_gly_itm_wtr[] ary) {
		this.ary = ary; this.ary_len = ary.length;
	}
	public void Bfr_arg__clear() {
		for (int i = 0; i < ary_len; ++i)
			ary[i].Clear();
		ary = null;
	}
	public boolean Bfr_arg__missing() {return ary == null;}
	public void Bfr_arg__add(Bry_bfr bfr) {
		for (int i = 0; i < ary_len; ++i) {
			Xoh_gly_itm_wtr itm = ary[i];
			itm.Bfr_arg__add(bfr);
		}
	}
}
