package net.loyin.ftl;

import java.io.IOException;
import java.io.Writer;
import java.text.MessageFormat;
import java.util.Map;

import net.loyin.util.PropertiesContent;
import net.loyin.util.ip.IPSeeker;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public class IpTarget extends BaseTarget {
	private static IPSeeker ipSeeker=  IPSeeker.getInstance();
	public void execute(Environment env, Map param, TemplateModel[] model,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		String ip=(String) param.get("ip");
		String country=ipSeeker.getCountry(ip);
		String area=ipSeeker.getArea(ip);
		Writer out= env.getOut();
		out.append(MessageFormat.format(PropertiesContent.config.get("ipinfostr"), country,area));
		body.render(out);
	}
}
