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
package gplx.xowa.xtns.scribunto; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
public class Scrib_proc_args {		
	private Keyval[] ary; private int ary_len;
	public Scrib_proc_args(Keyval[] v) {Init(v);}
	public int Len() {return ary_len;}
	public Keyval[] Ary() {return ary;}
	public boolean		Pull_bool(int i)				{return Bool_.cast(Get_or_fail(i));}
	public String	Pull_str(int i)					{return String_.cast(Get_or_fail(i));}
	public byte[]	Pull_bry(int i)					{return Bry_.new_u8(String_.cast(Get_or_fail(i)));}
	public Keyval[] Pull_kv_ary_safe(int idx)	{	// NOTE: must check for null array items; EX:[2:v2] -> [1:null,2:v2]; PAGE:en.d:Category:Nouns_by_language DATE:2016-04-29
		Keyval[] rv = (Keyval[])Get_or_fail(idx);
		List_adp list = null;
		int rv_len = rv.length;
		for (int i = 0; i < rv_len; ++i) {
			Keyval kv = rv[i];
			if (	kv.Key_tid() == Type_adp_.Tid__int		// luaj will be int
				|| 	kv.Key_tid() == Type_adp_.Tid__obj) {	// lua will be obj; note that luaj will also have other non-key objects
				Object key_obj = kv.Key_as_obj();
				if (key_obj.getClass() == Int_.Cls_ref_type) {  // key is int; cast it
					int key_int = Int_.cast(kv.Key_as_obj());
					if (key_int != i + 1) {					// key_int should match i; if not, then gaps exist; EX:[2:v2] should be 2nd element, not 1st
						if (list == null) {
							list = List_adp_.New();
							rv = null;
						}
						for (int j = 0; j < key_int - 1; ++j)	// add everything up to key_int as null; EX: [2:v2] -> [1:null]
							list.Add(Keyval_.int_(j + List_adp_.Base1, null));
					}
				}
			}
			if (rv == null)
				list.Add(kv);
		}
		if (rv == null)
			rv = (Keyval[])list.To_ary(Keyval.class);
		return rv;
	}
	public int		Pull_int(int i)					{Object rv = Get_or_fail(i);
		try {return Int_.coerce_(rv);} // coerce to handle "1" and 1; will still fail if "abc" is passed
		catch (Exception e) {
			Err_.Noop(e);
			throw Err_.new_wo_type("bad argument; int expected", "idx", i, "len", ary_len);
		}
	}	
	public long		Pull_long(int i)				{return (long)Pull_double(i);}
	public double	Pull_double(int i)				{Object rv = Get_or_fail(i);
		try {return Int_.coerce_(rv);} // coerce to handle "1" and 1; will still fail if "abc" is passed
		catch (Exception e) {
			Err_.Noop(e);
			try {return Double_.coerce_(rv);} // coerce to handle "1" and 1; will still fail if "abc" is passed
			catch (Exception e2) {
				Err_.Noop(e2);
				throw Err_.new_wo_type("bad argument; int expected", "idx", i, "len", ary_len);				
			}
		}
	}	
	public Object Pull_obj(int i) {return Get_or_fail(i);}
	public String	Cast_str_or(int i, String or)	{Object rv = Get_or_null(i); return rv == null ? or				: String_.cast		(rv);}
	public String	Cast_str_or_null(int i)			{Object rv = Get_or_null(i); return rv == null ? null			: String_.cast		(rv);}
	public byte[]	Cast_bry_or_null(int i)			{Object rv = Get_or_null(i); return rv == null ? null			: Bry_.new_u8(String_.cast	(rv));}	// NOTE: cast is deliberate; Scrib call checkType whi
	public byte[]	Cast_bry_or_empty(int i)		{Object rv = Get_or_null(i); return rv == null ? Bry_.Empty : Bry_.new_u8(String_.cast	(rv));}
	public byte[]	Cast_bry_or(int i, byte[] or)	{Object rv = Get_or_null(i); return rv == null ? or				: Bry_.new_u8(String_.cast	(rv));}
	public Object	Cast_obj_or_null(int i)			{return Get_or_null(i);}
	public boolean		Cast_bool_or_y(int i)			{Object rv = Get_or_null(i); return rv == null ? Bool_.Y		: Bool_.cast(rv);}
	public boolean		Cast_bool_or_n(int i)			{Object rv = Get_or_null(i); return rv == null ? Bool_.N		: Bool_.cast(rv);}
	public int		Cast_int_or(int i, int or)		{Object rv = Get_or_null(i); return rv == null ? or				: Int_.coerce_(rv);}	// coerce to handle "1" and 1;
	public String	Xstr_str_or_null(int i)			{Object rv = Get_or_null(i); return rv == null ? null			: Object_.Xto_str_loose_or(rv, null);}	// NOTE: Modules can throw exceptions in which return value is nothing; do not fail; return ""; EX: -logy; DATE:2013-10-14
	public byte[]	Xstr_bry_or_null(int i)			{Object rv = Get_or_null(i); return rv == null ? null			: Bry_.new_u8(Object_.Xto_str_loose_or(rv, null));}
	public Keyval[] Cast_kv_ary_or_null(int i)		{Object rv = Get_or_null(i); return rv == null ? null			: (Keyval[])rv;}
	public byte[][]	Cast_params_as_bry_ary_or_rest_of_ary(int params_idx)	{	// PAGE:ru.w:Ленин,_Владимир_Ильич; DATE:2014-07-01 MW:LanguageLibrary.php|ConvertPlural: if (is_array($args[0])) $args = $args[0];  $forms = array_values(array_map('strval', $args));
		if (params_idx < 0 || params_idx >= ary_len) return Bry_.Ary_empty;
		Object o = ary[params_idx].Val();
		if (Type_adp_.Eq_typeSafe(o, Keyval[].class)) {
			Keyval[] tbl = (Keyval[])o;
			int rv_len = tbl.length;
			byte[][] rv = new byte[rv_len][];
			for (int i = 0; i < rv_len; i++) {
				Keyval itm = tbl[i];
				rv[i] = Bry_.new_u8(String_.cast(itm.Val()));
			}
			return rv;
		}
		else {
			int rv_len = ary_len - params_idx;
			byte[][] rv = new byte[rv_len][];			
			for (int i = 0; i < rv_len; i++) {
				Keyval itm = ary[i + params_idx];
				rv[i] = Bry_.new_u8(String_.cast(itm.Val()));
			}
			return rv;
		}
	}
	public byte[] Extract_qry_args(Xowe_wiki wiki, int idx) {
		Object qry_args_obj = Cast_obj_or_null(idx);
		if (qry_args_obj == null) return Bry_.Empty;
		Class<?> qry_args_cls = Type_adp_.ClassOf_obj(qry_args_obj);
		if		(qry_args_cls == String.class)
			return Bry_.new_u8((String)qry_args_obj);
		else if (qry_args_cls == Keyval[].class) {
			Bry_bfr bfr = wiki.Utl__bfr_mkr().Get_b128();
			Keyval[] kvs = (Keyval[])qry_args_obj;
			int len = kvs.length;
			for (int i = 0; i < len; i++) {
				Keyval kv = kvs[i];
				if (i != 0) bfr.Add_byte(Byte_ascii.Amp);
				bfr.Add_str_u8(kv.Key());
				bfr.Add_byte(Byte_ascii.Eq);
				bfr.Add_str_u8(kv.Val_to_str_or_empty());
			}
			return bfr.To_bry_and_rls();
		}
		else {
			wiki.Appe().Usr_dlg().Warn_many("", "", "unknown type for GetUrl query args: ~{0}", Type_adp_.NameOf_type(qry_args_cls));
			return Bry_.Empty;
		}
	}
	private void Init(Keyval[] v) {
		int v_len = v.length;
		if (v_len == 0) {
			ary = Keyval_.Ary_empty;
			ary_len = 0;
			return;
		}
		int v_max = -1;
		for (int i = 0; i < v_len; i++) {
			Keyval kv = v[i];
			int idx = Int_.cast(kv.Key_as_obj());
			if (v_max < idx) v_max = idx;
		}
		this.ary_len = v_max;
		if (v_max == v_len) {		// keys are in sequential order; EX: [1:a,2:b,3:c]
			this.ary = v;
		}
		else {						// keys are not in sequential order, or there are gaps; EX: [1:a,3:c]
			ary = new Keyval[ary_len];
			for (int i = 0; i < v_len; i++) {
				Keyval kv = v[i];
				int idx = Int_.cast(kv.Key_as_obj());
				ary[idx - List_adp_.Base1] = kv;
			}
		}
	}
	private Object Get_or_null(int i) {
		if (i < 0 || i >= ary_len) return null;
		Keyval kv = ary[i];
		return kv == null ? null : kv.Val();
	}
	private Object Get_or_fail(int i) {
		if (i < 0 || i >= ary_len) throw Err_.new_wo_type("bad argument: nil", "idx", i, "len", ary_len);
		Keyval kv = ary[i];
		Object rv = kv == null ? null : kv.Val();
		if (rv == null) throw Err_.new_wo_type("scrib arg is null", "idx", i, "len", ary_len);
		return rv;
	}
}
