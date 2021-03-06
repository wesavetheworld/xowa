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
package gplx.xowa.addons.bldrs.centrals.dbs.datas.imports; import gplx.*; import gplx.xowa.*; import gplx.xowa.addons.*; import gplx.xowa.addons.bldrs.*; import gplx.xowa.addons.bldrs.centrals.*; import gplx.xowa.addons.bldrs.centrals.dbs.*; import gplx.xowa.addons.bldrs.centrals.dbs.datas.*;
import gplx.dbs.*;
public class Xobc_import_step_tbl implements Db_tbl {
	private final    Dbmeta_fld_list flds = new Dbmeta_fld_list();
	private final    String fld_step_id, fld_host_id, fld_wiki_abrv, fld_wiki_date, fld_import_name, fld_import_type, fld_import_zip, fld_import_md5, fld_import_size_zip, fld_import_size_raw, fld_prog_size_end, fld_prog_count_end;
	public final    Db_conn conn; private Db_stmt insert_stmt;
	public Xobc_import_step_tbl(Db_conn conn) {
		this.conn = conn;
		this.tbl_name				= "import_step";
		this.fld_step_id			= flds.Add_int_pkey("step_id");
		this.fld_host_id			= flds.Add_int("host_id");
		this.fld_wiki_abrv			= flds.Add_str("wiki_abrv", 255);
		this.fld_wiki_date			= flds.Add_str("wiki_date", 8);
		this.fld_import_name		= flds.Add_str("import_name", 255);
		this.fld_import_type		= flds.Add_int("import_type");
		this.fld_import_zip			= flds.Add_byte("import_zip");
		this.fld_import_size_zip	= flds.Add_long("import_size_zip");
		this.fld_import_size_raw 	= flds.Add_long("import_size_raw");
		this.fld_import_md5			= flds.Add_str("import_md5", 48);
		this.fld_prog_size_end 		= flds.Add_long("prog_size_end");
		this.fld_prog_count_end 	= flds.Add_long("prog_count_end");
		conn.Rls_reg(this);
	}
	public String Tbl_name() {return tbl_name;} private final    String tbl_name;
	public void Create_tbl() {conn.Meta_tbl_create(Dbmeta_tbl_itm.New(tbl_name, flds));}
	public Xobc_import_step_itm Select_one(int step_id) {
		Db_rdr rdr = conn.Stmt_select(tbl_name, flds, fld_step_id).Crt_int(fld_step_id, step_id).Exec_select__rls_auto();
		try {
			return (rdr.Move_next())
				? new Xobc_import_step_itm
				( rdr.Read_int(fld_step_id)
				, rdr.Read_int(fld_host_id)
				, rdr.Read_bry_by_str(fld_wiki_abrv)
				, rdr.Read_str(fld_wiki_date)
				, rdr.Read_str(fld_import_name)
				, rdr.Read_int(fld_import_type)
				, rdr.Read_byte(fld_import_zip)
				, rdr.Read_long(fld_import_size_zip)
				, rdr.Read_long(fld_import_size_raw)
				, rdr.Read_str(fld_import_md5)
				, rdr.Read_long(fld_prog_size_end)
				, rdr.Read_int(fld_prog_count_end)
				)
				: Xobc_import_step_itm.Null;
		}
		finally {rdr.Rls();}
	}
	public void Delete(int step_id) {
		conn.Stmt_delete(tbl_name, fld_step_id).Crt_int(fld_step_id, step_id).Exec_delete();
	}
	public void Insert(int step_id, int host_id, byte[] wiki_abrv, String wiki_date, String import_name, int import_type, byte zip_type, byte[] md5, long size_zip, long size_raw
		, long prog_size_end, int prog_count_end) {
		if (insert_stmt == null) insert_stmt = conn.Stmt_insert(tbl_name, flds);
		insert_stmt.Clear().Val_int(fld_step_id, step_id).Val_int(fld_host_id, host_id)
			.Val_bry_as_str(fld_wiki_abrv, wiki_abrv).Val_str(fld_wiki_date, wiki_date)
			.Val_str(fld_import_name, import_name)
			.Val_int(fld_import_type, import_type).Val_byte(fld_import_zip, zip_type)
			.Val_long(fld_import_size_zip, size_zip).Val_long(fld_import_size_raw, size_raw).Val_bry_as_str(fld_import_md5, md5)
			.Val_long(fld_prog_size_end, prog_size_end).Val_int(fld_prog_count_end, prog_count_end)
			.Exec_insert();
	}
	public void Select_tasks_steps(Xobc_task_step_hash task_step_hash, Xobc_step_map_tbl step_map_tbl, byte[] wiki_abrv, String wiki_date) {
		task_step_hash.Clear();
		Db_rdr rdr = conn.Stmt_sql(String_.Concat_lines_nl_skip_last
		( "SELECT  DISTINCT sm.task_id, sm.step_id"
		, "FROM    " + tbl_name + " imps"
		, "        JOIN " + step_map_tbl.Tbl_name() + " sm ON sm.step_id = imps.step_id"
		, "WHERE   imps.wiki_abrv = ?"
		, "AND     imps.wiki_date = ?"
		))
		.Crt_bry_as_str(fld_wiki_abrv, wiki_abrv)
		.Crt_str(fld_wiki_date, wiki_date)
		.Exec_select__rls_auto()
		;
		try {
			while (rdr.Move_next()) {
				task_step_hash.Add(rdr.Read_int("task_id"), rdr.Read_int("step_id"));
			}
		} finally {rdr.Rls();}
	}
	public void Rls() {
		insert_stmt = Db_stmt_.Rls(insert_stmt);
	}
}
