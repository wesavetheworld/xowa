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
package gplx.dbs; import gplx.*;
import gplx.dbs.qrys.*; import gplx.core.gfo_ndes.*;
public class Db_qry_fxt {
	public static void Insert_kvo(Db_conn conn, String tblName, Keyval_list kvList) {
		Db_qry_insert qry = Db_qry_.insert_(tblName);
		for (int i = 0; i < kvList.Count(); i++) {
			Keyval kv = kvList.Get_at(i);
			qry.Val_obj(kv.Key(), kv.Val());
		}
		qry.Exec_qry(conn);
	}
	public static GfoNde SelectAll(Db_conn conn, String tblName) {
		return Db_qry_.Exec_as_nde(conn, Db_qry_.select_tbl_(tblName));
	}
	public static int SelectAll_count(Db_conn conn, String tblName) {
		GfoNde nde = Db_qry_fxt.SelectAll(conn, tblName);
		return nde.Subs().Count();
	}
	public static void DeleteAll(Db_conn conn, String... ary) {
		for (String s : ary)
			Db_qry_.delete_tbl_(s).Exec_qry(conn);
	}
	public static void tst_Select(Db_conn conn, String tblName, Db_mock_row... expdAry) {
		GfoNde nde = Db_qry_fxt.SelectAll(conn, tblName);
		int len = Array_.Len(expdAry);
		for (int i = 0; i < len; i++) {
			Db_mock_row expdRow = expdAry[i];
			int actlIdx = (expdRow.Idx() == -1) ? i : expdRow.Idx();
			GfoNde actlNde = nde.Subs().FetchAt_asGfoNde(actlIdx);
			int fldLen = Array_.Len(expdRow.Dat());
			for (int j = 0; j < fldLen; j++) {
				Db_mock_cell expdDat = expdRow.Dat()[j];
				Object actlVal = expdDat.Fld() == null ? actlNde.ReadAt(j) : actlNde.Read(expdDat.Fld());
				Tfds.Eq(expdDat.Val(), actlVal);
			}
		}
	}
	public static String Db__print_tbl_as_str(Bry_bfr bfr, Db_conn conn, String tbl, String... cols) {
		int cols_len = cols.length;
		Db_rdr rdr = conn.Stmt_select(tbl, cols).Exec_select__rls_auto();
		while (rdr.Move_next()) {
			for (int i = 0; i < cols_len; ++i) {
				bfr.Add_obj(rdr.Read_at(i));
				bfr.Add_byte(i == cols_len - 1 ? Byte_ascii.Nl : Byte_ascii.Pipe);
			}
		}
		rdr.Rls();
		return bfr.To_str_and_clear();
	}
}
