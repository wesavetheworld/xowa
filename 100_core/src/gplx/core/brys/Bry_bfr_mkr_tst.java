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
package gplx.core.brys; import gplx.*; import gplx.core.*;
import org.junit.*;
public class Bry_bfr_mkr_tst {
	private final Bry_bfr_mkr_fxt fxt = new Bry_bfr_mkr_fxt();
	@Before public void setup() {fxt.Clear();}
	@Test   public void Get_1() 		{fxt.Clear().Get().Test__used(0);}
	@Test   public void Get_2()			{fxt.Clear().Get().Get().Test__used(0, 1);}
	@Test   public void Get_3()			{fxt.Clear().Get().Get().Get().Test__used(0, 1, 2);}
	@Test   public void Rls()			{fxt.Clear().Get().Rls(0).Test__used();}
	@Test   public void Rls_skip_1() {
		fxt.Clear().Get().Get().Rls(0).Test__used(-1, 1);
		fxt.Get().Test__used(0, 1);
	}
	@Test   public void Rls_skip_2_1() {
		fxt.Clear().Get().Get().Get().Rls(1).Rls(0).Test__used(-1, -1, 2);
		fxt.Get().Test__used(0, -1, 2);
		fxt.Get().Test__used(0, 1, 2);
		fxt.Get().Test__used(0, 1, 2, 3);
	}
	@Test   public void Get_rls_get() {	// PURPOSE: defect in which last rls failed b/c was not doing ++ if rv existed
		fxt.Clear().Get().Rls(0).Get().Get().Rls(1).Rls(0).Test__used();
	}
}
class Bry_bfr_mkr_fxt {
	private final Bry_bfr_mkr_mgr mkr = new Bry_bfr_mkr_mgr(Byte_.Zero, 32);
	public Bry_bfr_mkr_fxt Clear()		{mkr.Clear(); return this;}
	public Bry_bfr_mkr_fxt Get()		{mkr.Get(); return this;}
	public Bry_bfr_mkr_fxt Rls(int i)	{mkr.Used()[i].Mkr_rls(); return this;}
	public Bry_bfr_mkr_fxt Test__used(int... expd) {
		int actl_len = mkr.Used_len();
		int[] actl = new int[actl_len];
		for (int i = 0; i < actl_len; i++) {
			Bry_bfr bfr = mkr.Used()[i];
			int actl_val = bfr == null ? -2 : bfr.Mkr_idx();
			actl[i] = actl_val;
		}
		Tfds.Eq_ary(expd, actl);
		return this;
	}
}
