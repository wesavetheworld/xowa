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
package gplx.xowa.apps.apis.xowa.wikis; import gplx.*; import gplx.xowa.*; import gplx.xowa.apps.*; import gplx.xowa.apps.apis.*; import gplx.xowa.apps.apis.xowa.*;
import gplx.xowa.apps.apis.xowa.wikis.langs.*;
public class Xoapi_wiki_lang implements Gfo_invk {
	public Xoap_lang_variants	Variants()		{return variants;} private final    Xoap_lang_variants variants = new Xoap_lang_variants();
	public void Subscribe(Gfo_evt_itm sub) {variants.Subscribe(sub);}
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_variants)) 							return variants;
		else	return Gfo_invk_.Rv_unhandled;
	}
	private static final String Invk_variants = "variants";
}
