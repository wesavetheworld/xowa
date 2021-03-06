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
package gplx.xowa.wikis.tdbs; import gplx.*; import gplx.xowa.*; import gplx.xowa.wikis.*;
import org.junit.*; import gplx.xowa.wikis.tdbs.*;
import gplx.xowa.wikis.nss.*;
public class Xotdb_fsys_mgr_tst {
	@Before public void init() {fxt.Clear();} private final    Xow_fsys_mgr_fxt fxt = new Xow_fsys_mgr_fxt();
	@Test  public void Basic() {
		fxt.Zip_(Xotdb_dir_info_.Tid_page, Bool_.N).Url_ns_fil(Xotdb_dir_info_.Tid_page, Xow_ns_.Tid__main, 123, "mem/xowa/wiki/en.wikipedia.org/ns/000/page/00/00/00/01/0000000123.xdat");
		fxt.Zip_(Xotdb_dir_info_.Tid_page, Bool_.Y).Url_ns_fil(Xotdb_dir_info_.Tid_page, Xow_ns_.Tid__main, 123, "mem/xowa/wiki/en.wikipedia.org/ns/000/page_zip/00/00/00/01/0000000123.zip");
	}
}
class Xow_fsys_mgr_fxt {
	public void Clear() {
		app = Xoa_app_fxt.Make__app__edit();
		wiki = Xoa_app_fxt.Make__wiki__edit(app);
	}
	Xoae_app app; Xowe_wiki wiki;
	public Xow_fsys_mgr_fxt Zip_(byte tid, boolean v) {wiki.Tdb_fsys_mgr().Tdb_dir_regy()[tid].Ext_tid_(v ? gplx.core.ios.streams.Io_stream_.Tid_zip : gplx.core.ios.streams.Io_stream_.Tid_raw); return this;}
	public void Url_ns_fil(byte tid, int ns_id, int fil_idx, String expd) {
		Tfds.Eq(expd, wiki.Tdb_fsys_mgr().Url_ns_fil(tid, ns_id, fil_idx).Raw());
	}
}
