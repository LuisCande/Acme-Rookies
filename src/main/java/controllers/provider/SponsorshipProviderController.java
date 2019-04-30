
package controllers.provider;

import java.util.Calendar;
import java.util.Collection;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.PositionService;
import services.ProviderService;
import services.SponsorshipService;
import controllers.AbstractController;
import domain.Position;
import domain.Provider;
import domain.Sponsorship;

@Controller
@RequestMapping("sponsorship/provider")
public class SponsorshipProviderController extends AbstractController {

	//Services

	@Autowired
	private SponsorshipService	sponsorshipService;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private ProviderService		providerService;

	@Autowired
	private PositionService		positionService;


	//Creation

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		final ModelAndView result;
		final Sponsorship sponsorship;

		sponsorship = this.sponsorshipService.create();
		result = this.createEditModelAndView(sponsorship);

		return result;
	}

	//Edition

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int varId) {
		ModelAndView result;
		Sponsorship sponsorship;

		sponsorship = this.sponsorshipService.findOne(varId);

		if (sponsorship == null || sponsorship.getProvider().getId() != this.actorService.findByPrincipal().getId())
			return new ModelAndView("redirect:/welcome/index.do");

		result = this.createEditModelAndView(sponsorship);

		return result;
	}
	//Listing

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		final Collection<Sponsorship> sponsorships;
		final Provider provider = this.providerService.findOne(this.actorService.findByPrincipal().getId());

		//QUERY
		sponsorships = this.sponsorshipService.sponsorshipsFromProvider(provider.getId());

		result = new ModelAndView("sponsorship/list");
		result.addObject("sponsorships", sponsorships);
		result.addObject("requestURI", "sponsorship/provider/list.do");

		return result;
	}

	//Display 

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int varId) {
		ModelAndView result;
		Sponsorship sponsorship;

		sponsorship = this.sponsorshipService.findOne(varId);

		result = new ModelAndView("sponsorship/display");
		result.addObject("sponsorship", sponsorship);
		result.addObject("requestURI", "sponsorship/provider/display.do");

		return result;
	}

	//Delete GET

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int varId) {
		ModelAndView result;
		final Sponsorship sponsorship = this.sponsorshipService.findOne(varId);

		if (sponsorship == null || this.actorService.findByPrincipal().getId() != sponsorship.getProvider().getId())
			return new ModelAndView("redirect:/welcome/index.do");

		try {
			this.sponsorshipService.delete(sponsorship);
			result = new ModelAndView("redirect:/sponsorship/provider/list.do");

		} catch (final Throwable oops) {
			result = this.createEditModelAndView(sponsorship, "sponsorship.commit.error");
		}
		return result;
	}

	//Save

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Sponsorship sponsorship, final BindingResult binding) {
		ModelAndView result;

		try {
			sponsorship = this.sponsorshipService.reconstruct(sponsorship, binding);
		} catch (final ValidationException oops) {
			return this.createEditModelAndView(sponsorship);
		} catch (final Throwable oops) {
			return result = this.createEditModelAndView(sponsorship, "sponsorship.commit.error");
		}

		final int year = Calendar.getInstance().get(Calendar.YEAR);
		final int month = Calendar.getInstance().get(Calendar.MONTH);

		if (sponsorship.getCreditCard().getExpYear() < year)
			result = this.createEditModelAndView(sponsorship, "sponsorship.creditCard.error");
		else if (sponsorship.getCreditCard().getExpYear() == year && sponsorship.getCreditCard().getExpMonth() < month + 1)
			result = this.createEditModelAndView(sponsorship, "sponsorship.creditCard.error");
		else
			try {
				this.sponsorshipService.save(sponsorship);
				result = new ModelAndView("redirect:/sponsorship/provider/list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(sponsorship, "sponsorship.commit.error");
			}
		return result;
	}

	//Pay
	@RequestMapping(value = "/pay", method = RequestMethod.GET)
	public ModelAndView pay(@RequestParam final int varId) {
		ModelAndView result;
		Collection<Sponsorship> sponsorships;
		Sponsorship sponsorship;

		result = new ModelAndView("sponsorship/list");

		final Provider provider = (Provider) this.actorService.findByPrincipal();
		sponsorships = this.sponsorshipService.sponsorshipsFromProvider(provider.getId());

		sponsorship = this.sponsorshipService.findOne(varId);

		if (sponsorship.getProvider().getId() != this.actorService.findByPrincipal().getId())
			return new ModelAndView("redirect:/welcome/index.do");

		try {
			this.sponsorshipService.pay(sponsorship);
			sponsorships = this.sponsorshipService.sponsorshipsFromProvider(provider.getId());

			result.addObject("sponsorships", sponsorships);
			result.addObject("requestURI", "sponsorship/provider/list.do");

		} catch (final Throwable oops) {
			result = this.createEditModelAndView(sponsorship, "sponsorship.pay.error");
		}

		return result;
	}
	//Ancillary methods

	protected ModelAndView createEditModelAndView(final Sponsorship sponsorship) {
		ModelAndView result;

		result = this.createEditModelAndView(sponsorship, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Sponsorship sponsorship, final String messageCode) {
		ModelAndView result;

		final Collection<Position> positions = this.positionService.getPublicPositions();

		result = new ModelAndView("sponsorship/edit");
		result.addObject("sponsorship", sponsorship);
		result.addObject("positions", positions);
		result.addObject("message", messageCode);
		result.addObject("requestURI", "sponsorship/provider/edit.do");

		return result;

	}

}
