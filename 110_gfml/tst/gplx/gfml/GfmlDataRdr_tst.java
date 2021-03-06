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
package gplx.gfml; import gplx.*;
import org.junit.*; import gplx.core.stores.*;
public class GfmlDataRdr_tst {
	@Test  public void Raw() {
		raw = "root:{}";
		rdr = rdr_(raw);

		Tfds.Eq(rdr.NameOfNode(), "root");
	}
	@Test  public void Atrs() {
		raw = "root:id=1 name=me isPresent=true dateOf='2006-12-08';";
		rdr = rdr_(raw);

		Tfds.Eq(rdr.ReadInt("id"), 1);
		Tfds.Eq(rdr.ReadStr("name"), "me");
		Tfds.Eq(rdr.ReadBool("isPresent"), true);
		Tfds.Eq_date(rdr.ReadDate("dateOf"), DateAdp_.parse_gplx("2006-12-08"));
	}
	@Test  public void Subs() {
		raw = String_.Concat_any(
			"root:{",
			"	computers:id=1 {",
			"		item:name=cpu;",
			"		item:name=monitor;",
			"	}",
			"	person:id=2 {",
			"		item:name=hardDrive;",
			"		item:name=networkCard;",
			"	}",
			"}");
		rdr = rdr_(raw);

		DataRdr computers = rdr.Subs();
		int idx = 0;
		while (computers.MoveNextPeer()) {
			int expd = idx == 0 ? 1 : 2;
			Tfds.Eq(computers.ReadInt("id"), expd);
			idx++;
		}
		Tfds.Eq(idx, 2);

		DataRdr items = computers.Subs();
		idx = 0;
		while (items.MoveNextPeer()) {
			String expdStr = idx == 0 ? "hardDrive" : "networkCard";
			Tfds.Eq(items.ReadStr("name"), expdStr);
			idx++;
		}
		Tfds.Eq(idx, 2);
	}
	@Test  public void SelectRdr() {
		raw = String_.Concat_any(
			"root:{",
			"	person:name=me {}",
			"	computer:brand=noname {}",
			"}");
		rdr = rdr_(raw);

		DataRdr person = rdr.Subs_byName_moveFirst("person");
		Tfds.Eq(person.NameOfNode(), "person");
		Tfds.Eq(person.ReadStr("name"), "me");
		DataRdr computer = rdr.Subs_byName_moveFirst("computer");
		Tfds.Eq(computer.NameOfNode(), "computer");
		Tfds.Eq(computer.ReadStr("brand"), "noname");
	}
//		@Test  public void Subs_byKey() {
//			raw = String_.Concat_any(
//				"root:",
//				"	person=(name=me)",
//				";");
//			rdr = rdr_(raw);
//
//			DataRdr person = rdr.Subs_byKey("person");
//			Tfds.Eq(person.NameOfNode, "person");
//			Tfds.Eq(person.ReadStr("name"), "me");
//		}
//		@Test  public void Type() {
//			raw = String_.Concat_any(
//				"root:{",
//				"	_type:{example{explicit_val; bool_val; int_val; string_val; long_val; date_val; float_val; decimal_val;}}",
//				"	example:val1;",
//				"}");
//		
//			rdr = rdr_(raw);
//			DataRdr pointRdr = rdr.Subs(); rdr.MoveNextPeer();
//
//			Tfds.Eq(rdr.FieldCount, 8);
//			Tfds.Eq(rdr.ReadStr("explicit_val"), "val1");
//			Tfds.Eq(rdr.ReadBool("bool_val"), false);
//			Tfds.Eq(rdr.ReadInt("int_val"), 0);
//			Tfds.Eq(rdr.ReadStr("string_val"), null);
//			Tfds.Eq(rdr.ReadLongOrFail("long_val"), (long)0);
//			Tfds.Eq(rdr.ReadDate("date_val"), DateAdp_.MinValue);
//			Tfds.Eq(rdr.ReadFloat("float_val"), (float)0);
//			Tfds.Eq(rdr.FieldAt(1), "bool_val");
//			Tfds.Eq(rdr.Read(1), null);
//		}
	DataRdr rdr_(String raw) {
		DataRdr rootRdr = GfmlDataRdr.raw_root_(raw);
		return rootRdr;
	}
	String raw; DataRdr rdr;
}
