package net.loyin.controller.webadmin;

import net.loyin.jFinal.anatation.RouteBind;
import net.loyin.model.Acategory;
/**
 * 文章类别
 * @author 刘声凤
 *  2012-9-4 上午10:59:15
 */
@RouteBind(path="/ht/acate")
public class AcategoryController extends WebAdminBaseController {
	/**对应每个Controller的pages下的模块目录 已/结尾*/
	private static String ak="acategory/";
	public void index(){
		setAttr("page", Acategory.dao.paginate(getParaToInt(0, 1),20, "select *", "from acategory order by cate_id asc"));
		render(ak+"index.html");
	}
}
