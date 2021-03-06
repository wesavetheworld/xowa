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
package gplx.dbs.engines.postgres; import gplx.*; import gplx.dbs.*; import gplx.dbs.engines.*;
public class Postgres_conn_info extends Db_conn_info__base {
	public Postgres_conn_info(String raw, String db_api, String database, String server, String uid, String pwd) {super(raw, db_api, database);
		this.server = server;
		this.uid = uid;
		this.pwd = pwd;
	}
	@Override public String Key() {return Tid_const;} public static final String Tid_const = "postgresql";
	public String Server() {return server;} private final String server;
	public String Uid() {return uid;} private final String uid;
	public String Pwd() {return pwd;} private final String pwd;
	public static Db_conn_info new_(String server, String database, String uid, String pwd) {
		return Db_conn_info_.parse(Bld_raw
		( "gplx_key", Tid_const
		, "server", server
		, "database", database
		, "port", "5432"
		, "user id", uid
		, "password", pwd
		, "encoding", "unicode"	// needed for 1.1 conn; otherwise, ascii
		));
	}
	@Override public Db_conn_info New_self(String raw, Keyval_hash hash) {
		return new Postgres_conn_info
			( raw, Bld_api(hash, Keyval_.new_("encoding", "unicode")), hash.Get_val_as_str_or_fail("database")
			, hash.Get_val_as_str_or_fail("server"), hash.Get_val_as_str_or_fail("user id"), hash.Get_val_as_str_or_fail("password"));
	}
        public static final Postgres_conn_info Instance = new Postgres_conn_info("", "", "", "", "", "");
}
