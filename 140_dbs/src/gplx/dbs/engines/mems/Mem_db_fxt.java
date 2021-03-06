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
package gplx.dbs.engines.mems; import gplx.*; import gplx.dbs.*; import gplx.dbs.engines.*;
class Mem_db_fxt {
	public Mem_db_fxt() {
		Io_mgr.Instance.InitEngine_mem();
		Db_conn_bldr.Instance.Reg_default_mem();
	}
	public Db_conn			Make_conn(String url) {return Db_conn_bldr.Instance.Get_or_autocreate(Bool_.Y, Io_url_.mem_fil_(url));}
	public Dbmeta_tbl_itm	Exec__create_tbl(Db_conn conn, String tbl, String... fld_names) {
		Dbmeta_fld_list flds = new Dbmeta_fld_list();
		int len = fld_names.length;
		for (int i = 0; i < len; ++i)
			flds.Add_str(fld_names[i], 255);
		Dbmeta_tbl_itm rv = Dbmeta_tbl_itm.New(tbl, flds);
		conn.Meta_tbl_create(rv);
		return rv;
	}
	public void Exec__insert(Db_conn conn, String tbl_name, String[]... rows) {
		Mem_engine engine = (Mem_engine)conn.Engine();
		int rows_len = rows.length;
		Mem_tbl tbl = engine.Tbls__get(tbl_name);
		Dbmeta_fld_list flds_list = tbl.Meta().Flds().To_fld_list();
		int flds_len = flds_list.Len();
		Db_stmt stmt = conn.Stmt_insert(tbl_name, flds_list);
		for (int i = 0; i < rows_len; ++i) {
			stmt.Clear();
			String[] row = rows[i];
			for (int j = 0; j < flds_len; ++j)
				stmt.Val_str(flds_list.Get_at(j).Name(), row[j]);
			stmt.Exec_insert();
		}
	}
	public void Test__select(Db_conn conn, Db_qry qry, String[]... expd) {
		Db_stmt stmt = conn.Stmt_new(qry);
		Db_rdr rdr = new Mem_exec_select((Mem_engine)conn.Engine()).Select((Mem_stmt)stmt);
		List_adp actl_list = List_adp_.New();
		Bry_bfr tmp_bfr = Bry_bfr_.New();
		int expd_len = expd.length;
		String[] expd_rows = new String[expd_len];
		for (int i = 0; i < expd_len; ++i) {
			String[] expd_row = expd[i];
			for (int j = 0; j < expd_row.length; ++j) {
				if (j != 0) tmp_bfr.Add_byte_pipe();
				tmp_bfr.Add_str_u8(expd_row[j]);
			}
			expd_rows[i] = tmp_bfr.To_str_and_clear();
		}
		while (rdr.Move_next()) {
			int fld_len = rdr.Fld_len();
			for (int i = 0; i < fld_len; ++i) {
				if (i != 0) tmp_bfr.Add_byte_pipe();
				tmp_bfr.Add_obj_strict(rdr.Read_at(i));
			}
			actl_list.Add(tmp_bfr.To_str_and_clear());
		}			
		Tfds.Eq_ary(expd_rows, (String[])actl_list.To_ary_and_clear(String.class));
	}
}
