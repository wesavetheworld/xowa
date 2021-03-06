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
package gplx.core.ios; import gplx.*; import gplx.core.*;
import org.junit.*;
public class IoEngine_dir_deep_memory_tst extends IoEngine_dir_deep_base {
	@Override protected void setup_hook() {
		root = Io_url_.mem_dir_("mem/root");
	}	@Override protected IoEngine engine_() {return IoEngine_.Mem_init_();}
	@Test  @Override public void SearchDir() {
		super.SearchDir();
	}
	@Test  @Override public void MoveDirDeep() {
		super.MoveDirDeep();
	}
	@Test  @Override public void CopyDir() {
		super.CopyDir();
	}
	@Test  @Override public void DeleteDir() {
		super.DeleteDir();
	}
}
