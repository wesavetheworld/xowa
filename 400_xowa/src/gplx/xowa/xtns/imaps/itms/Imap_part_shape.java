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
package gplx.xowa.xtns.imaps.itms; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*; import gplx.xowa.xtns.imaps.*;
import gplx.core.primitives.*;
import gplx.xowa.parsers.*;
public class Imap_part_shape implements Imap_part, Imap_link_owner {
	public Imap_part_shape(byte shape_tid, Double_obj_val[] shape_pts) {
		this.shape_tid = shape_tid;
		this.shape_pts = shape_pts;
	}
	public byte				Part_tid() {return shape_tid;} private final    byte shape_tid;
	public Double_obj_val[] Shape_pts() {return shape_pts;} private final    Double_obj_val[] shape_pts;
	public int				Link_tid() {return link_tid;} private int link_tid;
	public Xop_tkn_itm		Link_tkn() {return link_tkn;} private Xop_tkn_itm link_tkn; public void	Link_tid_(int tid, Xop_tkn_itm tkn) {link_tid = tid; link_tkn = tkn;} 
	public byte[]			Link_href() {return link_href;} private byte[] link_href;	public void Link_href_(byte[] v) {this.link_href = v;}
	public byte[]			Link_text() {return link_text;} private byte[] link_text;	public void Link_text_(byte[] v) {this.link_text = v;}

	public static final byte Tid_default = 0, Tid_rect = 4, Tid_circle = 3, Tid_poly = 5;
}
