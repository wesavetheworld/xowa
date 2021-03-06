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
package gplx.xowa.parsers.vnts; import gplx.*; import gplx.xowa.*; import gplx.xowa.parsers.*;
class Vnt_flag_code_mgr {
	private final boolean[] ary = new boolean[Ary_len]; private final static int Ary_len = Vnt_flag_code_.Tid__max;
	public int Count() {return count;} private int count = 0;
	public boolean Get(int tid) {return ary[tid];}
	public void Clear() {
		count = 0;
		for (int i = 0; i < Ary_len; ++i)
			ary[i] = false;
	}
	public void Add(int tid) {
		this.Set_y(tid);
		++count;
	}
	public void Set_y(int tid) {ary[tid] = Bool_.Y;}
	public void Set_y_many(int... vals) {
		int len = vals.length;
		for (int i = 0; i < len; ++i)
			ary[vals[i]] = Bool_.Y;
	}
	public void Set_n(int tid) {ary[tid] = Bool_.N;}
	public void Limit(int tid) {
		for (int i = 0; i < Ary_len; ++i)
			ary[i] = i == tid;
	}
	public boolean Limit_if_exists(int tid) {
		boolean exists = ary[tid]; if (!exists) return false;
		this.Limit(tid);
		return true;
	}
	public void To_bfr__dbg(Bry_bfr bfr) {
		for (int i = 0; i < Ary_len; ++i) {
			if (ary[i]) {
				if (bfr.Len_gt_0()) bfr.Add_byte_semic();
				bfr.Add_str_a7(Vnt_flag_code_.To_str(i));
			}
		}
	}
}
