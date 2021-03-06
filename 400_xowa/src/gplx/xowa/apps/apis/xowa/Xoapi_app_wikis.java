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
package gplx.xowa.apps.apis.xowa; import gplx.*; import gplx.xowa.*; import gplx.xowa.apps.*; import gplx.xowa.apps.apis.*;
public class Xoapi_app_wikis implements Gfo_invk {
	private final    Ordered_hash hash = Ordered_hash_.New_bry();
	public Xoapi_app_wiki Get(byte[] domain) {
		Xoapi_app_wiki rv = (Xoapi_app_wiki)hash.Get_by(domain);
		if (rv == null) {
			rv = new Xoapi_app_wiki();
			hash.Add(domain, rv);
		}
		return rv;
	}
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_get)) 								return Get(m.ReadBry("v"));
		else	return Gfo_invk_.Rv_unhandled;
	}
	private static final String Invk_get = "get";
}
