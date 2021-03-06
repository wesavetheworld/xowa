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
package gplx.core.brys.args; import gplx.*; import gplx.core.*; import gplx.core.brys.*;
public class Bfr_arg__int implements Bfr_arg {
	private int val, val_digits;
	public Bfr_arg__int(int v) {Set(v);}
	public Bfr_arg__int Set(int v) {
		this.val = Int_.cast(v); 
		this.val_digits = Int_.DigitCount(val);
		return this;
	}
	public void Bfr_arg__add(Bry_bfr bfr) {bfr.Add_int_digits(val_digits, val);}
}
