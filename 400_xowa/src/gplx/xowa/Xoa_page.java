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
package gplx.xowa; import gplx.*;
import gplx.xowa.wikis.pages.*; import gplx.xowa.wikis.pages.lnkis.*;
public interface Xoa_page {
	Xow_wiki				Wiki();
	Xoa_url					Url(); byte[] Url_bry_safe();
	Xoa_ttl					Ttl();
	boolean					Exists();
	Xopg_revision_data		Revision_data();
	Xopg_html_data			Html_data();
	Xopg_lnki_list			Redlink_list();
	byte[]					Redirect_to_ttl(); void Redirect_to_ttl_(byte[] v);

	Xoa_page__commons_mgr	Commons_mgr();
	void					Xtn_gallery_packed_exists_y_();
	boolean					Xtn__timeline_exists();
	boolean					Xtn__gallery_exists();
}