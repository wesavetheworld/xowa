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
package gplx.xowa.xtns.math.texvcs.lxrs; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*; import gplx.xowa.xtns.math.*; import gplx.xowa.xtns.math.texvcs.*;
import gplx.xowa.xtns.math.texvcs.tkns.*; import gplx.xowa.xtns.math.texvcs.funcs.*;
class Texvc_lxr__curly_bgn implements Texvc_lxr {
	public int		Tid() {return Texvc_lxr_.Tid__curly_bgn;}
	public int		Make_tkn(Texvc_ctx ctx, Texvc_root root, byte[] src, int src_len, int bgn_pos, int cur_pos) {
		int uid = root.Regy__add(Texvc_tkn_.Tid__curly, Texvc_tkn_mkr.Singleton_id__null, bgn_pos, cur_pos, new Texvc_tkn__func(Texvc_func_itm_.Itm__arg));
		ctx.Stack().Add(uid);
		return cur_pos;
	}
}
class Texvc_lxr__curly_end implements Texvc_lxr {
	public int		Tid() {return Texvc_lxr_.Tid__curly_end;}
	public int		Make_tkn(Texvc_ctx ctx, Texvc_root root, byte[] src, int src_len, int bgn_pos, int cur_pos) {
		int bgn_uid = ctx.Stack().Pop_or_fail();
		root.Regy__take_from_root_end(bgn_uid);
		root.Regy__update_end(bgn_uid, cur_pos);
		return cur_pos;
	}
}
