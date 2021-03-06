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
package gplx.langs.mustaches; import gplx.*; import gplx.langs.*;
import org.junit.*;
public class Mustache_tkn_parser_tst {
	private final    Mustache_tkn_parser_fxt fxt = new Mustache_tkn_parser_fxt();
	@Test  public void Basic() {
		fxt.Test_parse("a{{b}}c", "ac");
	}
	@Test  public void Comment() {
		fxt.Test_parse("a{{!b}}c", "ac");
	}
}
class Mustache_tkn_parser_fxt {
	private final    Mustache_tkn_parser parser = new Mustache_tkn_parser();
	private final    Mustache_render_ctx ctx = new Mustache_render_ctx();
	private final    Mustache_bfr bfr = Mustache_bfr.New();
	public void Test_parse(String src_str, String expd) {
		byte[] src_bry = Bry_.new_a7(src_str);
		Mustache_tkn_itm actl_itm = parser.Parse(src_bry, 0, src_bry.length);
		actl_itm.Render(bfr, ctx);
		Tfds.Eq_str_lines(expd, bfr.To_str_and_clear());
	}
}
