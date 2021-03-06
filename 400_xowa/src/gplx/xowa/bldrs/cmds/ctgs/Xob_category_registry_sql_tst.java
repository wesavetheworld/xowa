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
package gplx.xowa.bldrs.cmds.ctgs; import gplx.*; import gplx.xowa.*; import gplx.xowa.bldrs.*; import gplx.xowa.bldrs.cmds.*;
import org.junit.*; import gplx.core.primitives.*; import gplx.core.encoders.*; import gplx.xowa.bldrs.*; import gplx.xowa.wikis.nss.*;
public class Xob_category_registry_sql_tst {
	@Before	public void init() {if (Xoa_test_.Db_skip()) return; fxt.Clear();} private Xob_category_registry_sql_fxt fxt = new Xob_category_registry_sql_fxt();
	@After public void term() {if (Xoa_test_.Db_skip()) return; fxt.Rls();}
	@Test   public void Basic() {
		if (Xoa_test_.Db_skip()) return;
		fxt.Init_page_insert(String_.Ary("Ctg3", "Ctg2", "Ctg1"));
		fxt.Exec_category_registry_cmd();
		fxt.Test_ids(Int_.Ary(3, 2, 1));	// note that Ctg1 is page_id 3
	}
}
class Xob_category_registry_sql_fxt {
	Db_mgr_fxt fxt; Xowe_wiki wiki; Xoae_app app; Int_obj_ref page_id_next = Int_obj_ref.New(1);
	public void Clear() {
		if (fxt == null) {
			fxt = new Db_mgr_fxt().Ctor_fsys();
			fxt.Init_db_sqlite();
			wiki = fxt.Wiki();
			app = wiki.Appe();
		}
	}
	public void Rls() {fxt.Rls();}
	public void Init_page_insert(String[] ttls) {
		fxt.Init_page_insert(page_id_next, Xow_ns_.Tid__category, ttls);
	}
	public void Exec_category_registry_cmd() {
		app.Bldr().Cmd_mgr().Add_cmd(wiki, Xob_cmd_keys.Key_text_cat_core);
		app.Bldr().Run();
	}
	public void Test_ids(int[] expd) {
		Io_url rslts_dir = Xob_category_registry_sql.Tmp_dir(wiki);
		String rslts_txt = Io_mgr.Instance.LoadFilStr(Io_mgr.Instance.QueryDir_fils(rslts_dir)[0]);
		int[] actl = Parse_rslts_txt(rslts_txt);
		Tfds.Eq_ary(expd, actl);
	}
	int[] Parse_rslts_txt(String rslts_txt) {
		String[] lines = String_.SplitLines_nl(rslts_txt);
		List_adp list = List_adp_.New();
		int len = lines.length;
		for (int i = 0; i < len; i++) {
			String line = lines[i];
			if (String_.Len_eq_0(line)) break;	// ignore blank line
			String[] flds = String_.Split(line, '|');
			list.Add(Base85_.To_int_by_str(flds[1]));
		}
		return (int[])list.To_ary_and_clear(int.class);
	}
}
