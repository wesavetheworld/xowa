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
package gplx.dbs.sqls.itms; import gplx.*; import gplx.dbs.*; import gplx.dbs.sqls.*;
public class Db_obj_ary_fld {
	public Db_obj_ary_fld(String name, int type_tid) {this.name = name; this.type_tid = type_tid;}
	public String Name() {return name;} public Db_obj_ary_fld Name_(String v) {name = v; return this;} private String name;
	public int Type_tid() {return type_tid;} public Db_obj_ary_fld Type_tid_(int v) {type_tid = v; return this;} private int type_tid;
}
