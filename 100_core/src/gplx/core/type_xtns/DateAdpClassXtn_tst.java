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
package gplx.core.type_xtns; import gplx.*; import gplx.core.*;
import org.junit.*;
public class DateAdpClassXtn_tst {
	@Test  public void XtoDb() {
		tst_XtoDb("20091115 220102.999", "2009-11-15 22:01:02.999");
	}
	void tst_XtoDb(String val, String expdRaw) {
		String actlRaw = (String)DateAdpClassXtn.Instance.XtoDb(DateAdp_.parse_gplx(val));
		Tfds.Eq(expdRaw, actlRaw);
	}
}
