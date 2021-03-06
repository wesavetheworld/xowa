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
package gplx.xowa.guis.tabs; import gplx.*; import gplx.xowa.*; import gplx.xowa.guis.*;
public class Xog_tab_mgr__swt implements Xog_tab_mgr {
	private final    Xoa_gui_mgr gui_mgr;
	public Xog_tab_mgr__swt(Xoa_gui_mgr gui_mgr) {this.gui_mgr = gui_mgr;}
	public void New_tab(boolean focus, String site, String page) {			
		// gui_mgr.Browser_win().Tab_mgr().Tabs_new_link(url, focus);	// TODO_OLD: handle html dumps
	}
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Xog_tab_mgr_.Invk__new_tab))		gui_mgr.Kit().New_cmd_sync(this).Invk(ctx, ikey, Invk__new_tab_async, m);
		else if	(ctx.Match(k, Invk__new_tab_async))				this.New_tab(m.ReadYn("focus"), m.ReadStr("site"), m.ReadStr("page"));
		else	return Gfo_invk_.Rv_unhandled;
		return this;
	}
	private static final String Invk__new_tab_async = "new_tab_async";
}
