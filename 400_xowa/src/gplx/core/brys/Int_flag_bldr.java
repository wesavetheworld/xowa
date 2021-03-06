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
package gplx.core.brys; import gplx.*; import gplx.core.*;
public class Int_flag_bldr {
	private int[] pow_ary;
	public int[] Val_ary() {return val_ary;} private int[] val_ary;
	public Int_flag_bldr Pow_ary_bld_(int... ary)	{
		this.pow_ary = Int_flag_bldr_.Bld_pow_ary(ary);
		this.val_ary = new int[pow_ary.length];
		return this;
	}
	public boolean Set_as_bool(int idx, boolean val)		{val_ary[idx] = val ? 1 : 0; return val;}
	public byte Set_as_byte(int idx, byte val)		{val_ary[idx] = val; return val;}
	public int Set_as_int(int idx, int val)			{val_ary[idx] = val; return val;}
	public Int_flag_bldr Set(int idx, boolean val)		{Set_as_bool(idx, val); return this;}
	public Int_flag_bldr Set(int idx, byte val)		{Set_as_byte(idx, val); return this;}
	public Int_flag_bldr Set(int idx, int val)		{Set_as_int(idx, val); return this;}
	public int Get_as_int(int idx)					{return val_ary[idx];}
	public byte Get_as_byte(int idx)				{return (byte)val_ary[idx];}
	public boolean Get_as_bool(int idx)				{return val_ary[idx] == 1;}
	public int Encode()								{return Int_flag_bldr_.To_int(pow_ary, val_ary);}
	public void Decode(int v)						{Int_flag_bldr_.To_int_ary(val_ary, pow_ary, v);}
}
