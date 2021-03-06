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
package gplx.xowa.htmls.bridges.dbuis.fmtrs; import gplx.*; import gplx.xowa.*; import gplx.xowa.htmls.*; import gplx.xowa.htmls.bridges.*; import gplx.xowa.htmls.bridges.dbuis.*;
import gplx.xowa.htmls.bridges.dbuis.tbls.*;
import org.junit.*;
public class Dbui_tbl_fmtr_tst {
	@Before public void init() {fxt.Clear();} private final    Dbui_tbl_fmtr_fxt fxt = new Dbui_tbl_fmtr_fxt();
	@Test  public void Basic() {
//			fxt.Test_write
//			( fxt.Make_tbl()
//			, String_.Concat_lines_nl_skip_last()
//			);
	}
}
class Dbui_tbl_fmtr_fxt {
	private final    Bry_bfr bfr = Bry_bfr_.New_w_size(255);
	private final    Dbui_tbl_fmtr tbl_fmtr = new Dbui_tbl_fmtr();
	public void Clear() {}
	public Dbui_tbl_itm Make_tbl() {
		return null;
	}
	public void Test_write(Dbui_tbl_itm tbl, String expd) {
		tbl_fmtr.Write(bfr, tbl, null, null, null);
		Tfds.Eq_str_lines(expd, bfr.To_str_and_clear());
	}
}
