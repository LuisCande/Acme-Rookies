
package controllers.actor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import security.Authority;
import services.ActorService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import domain.Actor;
import domain.Administrator;
import domain.Auditor;
import domain.Company;
import domain.Provider;
import domain.Rookie;

@Controller
@RequestMapping("/download")
public class DownloadController {

	@Autowired
	ActorService				actorService;

	private static final String	APPLICATION_PDF	= "application/pdf";


	@RequestMapping(value = "/myPersonalData", method = RequestMethod.GET, produces = DownloadController.APPLICATION_PDF)
	public @ResponseBody
	void downloadMyPersonalData(final HttpServletResponse response) throws IOException, DocumentException {

		final File file = new File("MyPersonalData.pdf");
		final FileOutputStream fileout = new FileOutputStream(file);
		final Document document = new Document();
		PdfWriter.getInstance(document, fileout);
		document.addAuthor("Actor");
		document.addTitle("My personal data");
		document.open();

		final Actor principal = this.actorService.findByPrincipal();

		final Company company = new Company();
		final Authority authCompany = new Authority();
		authCompany.setAuthority(Authority.COMPANY);

		final Rookie rookie = new Rookie();
		final Authority authRookie = new Authority();
		authRookie.setAuthority(Authority.ROOKIE);

		final Provider provider = new Provider();
		final Authority authProvider = new Authority();
		authProvider.setAuthority(Authority.PROVIDER);

		final Auditor auditor = new Auditor();
		final Authority authAuditor = new Authority();
		authAuditor.setAuthority(Authority.AUDITOR);

		final Administrator admin = new Administrator();
		final Authority authAdmin = new Authority();
		authAdmin.setAuthority(Authority.ADMIN);

		final ObjectMapper mapper = new ObjectMapper();

		final Paragraph paragraph = new Paragraph();
		paragraph.add(principal.toString());
		paragraph.setAlignment(Element.ALIGN_LEFT);
		document.add(paragraph);

		if (principal != null)
			if (principal.getUserAccount().getAuthorities().contains(authCompany)) {
				final Company companyPrincipal = (Company) this.actorService.findByPrincipal();
				company.setName(companyPrincipal.getName());
				company.setSurnames(companyPrincipal.getSurnames());
				company.setVatNumber(companyPrincipal.getVatNumber());
				company.setCreditCard(companyPrincipal.getCreditCard());
				company.setPhoto(companyPrincipal.getPhoto());
				company.setEmail(companyPrincipal.getEmail());
				company.setPhone(companyPrincipal.getPhone());
				company.setAddress(companyPrincipal.getAddress());
				company.setCommercialName(companyPrincipal.getCommercialName());
				final String json = mapper.writeValueAsString(companyPrincipal);
				paragraph.add(json);
			} else if (principal.getUserAccount().getAuthorities().contains(authRookie)) {
				final Rookie rookiePrincipal = (Rookie) this.actorService.findByPrincipal();
				rookie.setName(rookiePrincipal.getName());
				rookie.setSurnames(rookiePrincipal.getSurnames());
				rookie.setVatNumber(rookiePrincipal.getVatNumber());
				rookie.setCreditCard(rookiePrincipal.getCreditCard());
				rookie.setPhoto(rookiePrincipal.getPhoto());
				rookie.setEmail(rookiePrincipal.getEmail());
				rookie.setPhone(rookiePrincipal.getPhone());
				rookie.setAddress(rookiePrincipal.getAddress());
				final String json = mapper.writeValueAsString(rookie);
				paragraph.add(json);
			} else if (principal.getUserAccount().getAuthorities().contains(authProvider)) {
				final Provider providerPrincipal = (Provider) this.actorService.findByPrincipal();
				provider.setName(providerPrincipal.getName());
				provider.setSurnames(providerPrincipal.getSurnames());
				provider.setVatNumber(providerPrincipal.getVatNumber());
				provider.setCreditCard(providerPrincipal.getCreditCard());
				provider.setPhoto(providerPrincipal.getPhoto());
				provider.setEmail(providerPrincipal.getEmail());
				provider.setPhone(providerPrincipal.getPhone());
				provider.setAddress(providerPrincipal.getAddress());
				provider.setMakeP(providerPrincipal.getMakeP());
				final String json = mapper.writeValueAsString(provider);
				paragraph.add(json);
			} else if (principal.getUserAccount().getAuthorities().contains(authAuditor)) {
				final Auditor auditorPrincipal = (Auditor) this.actorService.findByPrincipal();
				auditor.setName(auditorPrincipal.getName());
				auditor.setSurnames(auditorPrincipal.getSurnames());
				auditor.setVatNumber(auditorPrincipal.getVatNumber());
				auditor.setCreditCard(auditorPrincipal.getCreditCard());
				auditor.setPhoto(auditorPrincipal.getPhoto());
				auditor.setEmail(auditorPrincipal.getEmail());
				auditor.setPhone(auditorPrincipal.getPhone());
				auditor.setAddress(auditorPrincipal.getAddress());
				final String json = mapper.writeValueAsString(auditor);
				paragraph.add(json);
			} else if (principal.getUserAccount().getAuthorities().contains(authAdmin)) {
				final Administrator adminPrincipal = (Administrator) this.actorService.findByPrincipal();
				admin.setName(adminPrincipal.getName());
				admin.setSurnames(adminPrincipal.getSurnames());
				admin.setVatNumber(adminPrincipal.getVatNumber());
				admin.setCreditCard(adminPrincipal.getCreditCard());
				admin.setPhoto(adminPrincipal.getPhoto());
				admin.setEmail(adminPrincipal.getEmail());
				admin.setPhone(adminPrincipal.getPhone());
				admin.setAddress(adminPrincipal.getAddress());
				final String json = mapper.writeValueAsString(admin);
				paragraph.add(json);
			}

		document.add(paragraph);
		document.close();

		final InputStream in = new FileInputStream(file);

		response.setContentType(DownloadController.APPLICATION_PDF);
		response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
		response.setHeader("Content-Length", String.valueOf(file.length()));

		FileCopyUtils.copy(in, response.getOutputStream());

	}

}
