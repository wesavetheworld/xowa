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
package gplx.xowa.addons.bldrs.exports.splits.srchs; import gplx.*; import gplx.xowa.*; import gplx.xowa.addons.*; import gplx.xowa.addons.bldrs.*; import gplx.xowa.addons.bldrs.exports.*; import gplx.xowa.addons.bldrs.exports.splits.*;
import gplx.dbs.*;
import gplx.xowa.addons.bldrs.exports.splits.rslts.*;
class Split_rslt_wkr__link extends Split_rslt_wkr__objs__base {
	@Override public byte Tid() {return Split_rslt_tid_.Tid__srch_link;}
	@Override public String Tbl_name() {return "rslt_srch_link";}
	@Override public Dbmeta_fld_itm[] Pkey_flds() {
		return new Dbmeta_fld_itm[] {Dbmeta_fld_itm.new_int("word_id"), Dbmeta_fld_itm.new_int("page_id")};
	}
}
