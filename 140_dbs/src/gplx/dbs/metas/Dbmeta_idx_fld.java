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
package gplx.dbs.metas; import gplx.*; import gplx.dbs.*;
public class Dbmeta_idx_fld {
	public Dbmeta_idx_fld(String name, int sort_tid) {this.Name = name; this.Sort_tid = sort_tid;}
	public String Name;
	public int Sort_tid;
	public boolean Eq(Dbmeta_idx_fld comp) {
		return	String_.Eq(Name, comp.Name)
			&&	Sort_tid == comp.Sort_tid;
	}

	public static final    Dbmeta_idx_fld[] Ary_empty = new Dbmeta_idx_fld[0];
	public static final int Sort_tid__none = 0, Sort_tid__asc = 1, Sort_tid__desc = 2;
	public static boolean Ary_eq(Dbmeta_idx_fld[] lhs_ary, Dbmeta_idx_fld[] rhs_ary) {
		int lhs_len = lhs_ary.length, rhs_len = rhs_ary.length;
		if (lhs_len != rhs_len) return false;
		for (int i = 0; i < lhs_len; ++i)
			if (!lhs_ary[i].Eq(rhs_ary[i])) return false;
		return true;
	}

	public static Dbmeta_idx_fld Dsc(String name) {return new Dbmeta_idx_fld(name, Sort_tid__desc);}
}
