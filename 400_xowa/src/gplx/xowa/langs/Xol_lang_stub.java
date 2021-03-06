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
package gplx.xowa.langs; import gplx.*; import gplx.xowa.*;
public class Xol_lang_stub {
	public Xol_lang_stub(int id, byte[] key, byte[] canonical_name) {this.id = id; this.key = key; this.canonical_name = canonical_name;}
	public int		Id() {return id;} private final int id;											// EX: 1
	public byte[]	Key() {return key;} private final byte[] key;									// EX: de
	public byte[]	Canonical_name() {return canonical_name;} private final byte[] canonical_name;	// EX: Deutsch
}
