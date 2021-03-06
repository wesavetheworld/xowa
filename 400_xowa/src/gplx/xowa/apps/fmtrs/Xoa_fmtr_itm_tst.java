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
package gplx.xowa.apps.fmtrs; import gplx.*; import gplx.xowa.*; import gplx.xowa.apps.*;
import org.junit.*; import gplx.langs.gfs.*;
public class Xoa_fmtr_itm_tst {
	@Before public void init() {fxt.Clear();} private Xoa_fmtr_itm_fxt fxt = new Xoa_fmtr_itm_fxt();
	@Test   public void Basic() {
		fxt.Init_src("app.wikis;");
		fxt.Init_fmt("domain=~{<>domain;<>};");
		fxt.Test_run("domain=en.wikipedia.org;");
	}
}
class Xoa_fmtr_itm_fxt {
	private Xoae_app app; private Xoa_fmtr_itm itm;
	public void Clear() {
		app = Xoa_app_fxt.Make__app__edit();
		Xoa_app_fxt.Make__wiki__edit(app);	// create enwiki
		itm = new Xoa_fmtr_itm(app);
		GfsCore.Instance.MsgParser_(gplx.langs.gfs.Gfs_msg_bldr.Instance);
	}
	public Xoa_fmtr_itm_fxt Init_src(String v) {itm.Src_(v); return this;}
	public Xoa_fmtr_itm_fxt Init_fmt(String v) {itm.Fmt_(Bry_.new_a7(v)); return this;}
	public void Test_run(String expd) {
		String actl = itm.Run();
		Tfds.Eq(expd, actl);
	}
}
