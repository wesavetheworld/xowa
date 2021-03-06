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
package gplx.gfui.controls.standards; import gplx.*; import gplx.gfui.*; import gplx.gfui.controls.*;
import gplx.langs.gfs.*;
import gplx.gfui.draws.*; import gplx.gfui.gfxs.*; import gplx.gfui.imgs.*; import gplx.gfui.kits.core.*; import gplx.gfui.controls.gxws.*; import gplx.gfui.controls.elems.*; import gplx.gfui.controls.windows.*;
public class GfuiBtn extends GfuiElemBase {
	@Override public boolean Click_able() {return true;}
	@Override public void Click() {GfuiBtn.DoThis(this, clickMsg, clickInvkCmd);}
	public GfuiBtn Click_msg_(GfoMsg v) {clickMsg = v; return this;} GfoMsg clickMsg;
	public GfuiBtn Click_invk(Gfo_invk_cmd v) {clickInvkCmd = v; return this;} Gfo_invk_cmd clickInvkCmd;
	@Override public boolean PaintCbk(PaintArgs args) {
		super.PaintCbk(args);
		if (this.Focus_has()) focusBorder.DrawData(args.Graphics());
		this.TextMgr().DrawData(args.Graphics());
		return true;
	}	GfuiBorderMgr focusBorder;
	@Override public boolean SizeChangedCbk()	{super.SizeChangedCbk(); GfuiBtn_.FocusBorderRect_set(focusBorder, this); this.Redraw(); return true;}
	@Override public boolean FocusGotCbk()		{super.FocusGotCbk(); this.Redraw(); return true;}	// Redraw for focusBorder
	@Override public boolean FocusLostCbk()		{super.FocusLostCbk(); this.Redraw(); return true;}	// Redraw for focusBorder
	public ImageAdp Btn_img() {
		Object o = Gfo_invk_.Invk_by_key(UnderElem(), Invk_btn_img);
		return o == UnderElem() ? null : (ImageAdp)o;	// NOTE: lgc guard
	}	public GfuiBtn Btn_img_(ImageAdp v) {Gfo_invk_.Invk_by_val(UnderElem(), Invk_btn_img_, v); return this;}
	@Override public GxwElem UnderElem_make(Keyval_hash ctorArgs) {return GxwElemFactory_.Instance.control_();}
	@Override public void ctor_GfuiBox_base(Keyval_hash ctorArgs) {
		focusBorder = GfuiBorderMgr.new_().All_(PenAdp_.new_(ColorAdp_.Gray, 1));
		super.ctor_GfuiBox_base(ctorArgs);
		this.TextMgr().AlignH_(GfuiAlign_.Mid);
		this.Border().All_(PenAdp_.black_()); this.Border().Bounds_sync(RectAdp_.size_(this.Size().Op_subtract(1)));
		GfuiBtn_.FocusBorderRect_set(focusBorder, this);
		Inject_(GfuiBtnClickBnd.Instance);
		Inject_(GfuiFocusXferBnd.Instance);
		this.CustomDraw_set(true);
	}
	@Override public void ctor_kit_GfuiElemBase(Gfui_kit kit, String key, GxwElem underElem, Keyval_hash ctorArgs) {
		this.kit = kit;
		super.ctor_kit_GfuiElemBase(kit, key, underElem, ctorArgs);
		focusBorder = GfuiBorderMgr.new_().All_(PenAdp_.new_(ColorAdp_.Gray, 1));
		Inject_(GfuiBtnClickBnd.Instance);
		Inject_(GfuiFocusXferBnd.Instance);
	}	Gfui_kit kit;
	@gplx.Internal protected static void DoThis(GfuiElem click, GfoMsg clickMsg, Gfo_invk_cmd clickInvkCmd) {
		try {
			if (clickInvkCmd != null) {
				GfsCtx ctx = GfsCtx.new_().MsgSrc_(click);
				clickInvkCmd.Exec_by_ctx(ctx, GfoMsg_.Null);
			}
			else if (clickMsg != null && clickMsg != GfoMsg_.Null) {
				GfsCtx ctx = GfsCtx.new_().MsgSrc_(click);
				if (String_.Eq(clickMsg.Key(), "."))
					GfsCore.Instance.ExecOne_to(ctx, click, clickMsg.Subs_getAt(0));
				else
					GfsCore.Instance.ExecOne(ctx, clickMsg);
			}
		}	catch (Exception e) {GfuiEnv_.ShowMsg(Err_.Message_gplx_full(e));}
	}
	public static final String Invk_btn_img = "btn_img", Invk_btn_img_ = "btn_img_";
	public static final String CFG_border_on_ = "border_on_";
}
