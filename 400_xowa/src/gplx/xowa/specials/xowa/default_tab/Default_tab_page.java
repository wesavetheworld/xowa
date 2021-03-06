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
package gplx.xowa.specials.xowa.default_tab; import gplx.*; import gplx.xowa.*; import gplx.xowa.specials.*; import gplx.xowa.specials.xowa.*;
public class Default_tab_page implements Xow_special_page {
	public Xow_special_meta Special__meta() {return Xow_special_meta_.Itm__default_tab;}
	public void Special__gen(Xow_wiki wiki, Xoa_page pagei, Xoa_url url, Xoa_ttl ttl) {
		Xoae_page page = (Xoae_page)pagei;
		page.Db().Text().Text_bry_(Bry_.Empty);
		page.Html_data().Custom_html_(Bry_.Empty);
		page.Html_data().Custom_tab_name_(Tab_name_bry);
	}
	public static final    byte[] Tab_name_bry = Bry_.new_a7("New Tab");

	public Xow_special_page Special__clone() {return this;}
}
