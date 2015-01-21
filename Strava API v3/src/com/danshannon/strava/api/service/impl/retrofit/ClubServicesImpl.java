/**
 * 
 */
package com.danshannon.strava.api.service.impl.retrofit;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

import com.danshannon.strava.api.model.Activity;
import com.danshannon.strava.api.model.Athlete;
import com.danshannon.strava.api.model.Club;
import com.danshannon.strava.api.service.ClubServices;
import com.danshannon.strava.api.service.Strava;
import com.danshannon.strava.api.service.exception.NotFoundException;
import com.danshannon.strava.api.service.exception.UnauthorizedException;
import com.danshannon.strava.util.Paging;
import com.danshannon.strava.util.impl.gson.JsonUtilImpl;

/**
 * @author danshannon
 *
 */
public class ClubServicesImpl implements ClubServices {
	private static RestAdapter.LogLevel LOG_LEVEL = RestAdapter.LogLevel.FULL;
	
	private ClubServicesImpl(ClubServicesRetrofit restService) {
		this.restService = restService;
	}
	
	/**
	 * <p>Returns an implementation of {@link ClubServices club services}</p>
	 * 
	 * <p>Instances are cached so that if 2 requests are made for the same token, the same instance is returned</p>
	 * 
	 * @param token The Strava access token to be used in requests to the Strava API
	 * @return An implementation of the club services
	 * @throws UnauthorizedException If the token used to create the service is invalid
	 */
	public static ClubServices implementation(final String token) throws UnauthorizedException {
		ClubServices restService = restServices.get(token);
		if (restService == null) {
			restService = new ClubServicesImpl(new RestAdapter.Builder()
				.setConverter(new GsonConverter(new JsonUtilImpl().getGson()))
				.setLogLevel(LOG_LEVEL)
				.setEndpoint(Strava.ENDPOINT)
				.setRequestInterceptor(new RequestInterceptor() {
					@Override
					public void intercept(RequestFacade request) {
						request.addHeader("Authorization", "Bearer " + token);
					}
				})
				.setErrorHandler(new RetrofitErrorHandler())
				.build()
				.create(ClubServicesRetrofit.class));

			// Check that the token works (i.e. it is valid)
			restService.listAuthenticatedAthleteClubs();

			// Store the token for later retrieval so that there's only one service per token
			restServices.put(token, restService);
			
		}
		return restService;
	}
	
	private static HashMap<String,ClubServices> restServices = new HashMap<String,ClubServices>();
	
	private ClubServicesRetrofit restService;
	

	/**
	 * @see com.danshannon.strava.api.service.ClubServices#getClub(java.lang.Integer)
	 */
	@Override
	public Club getClub(Integer id) throws NotFoundException {
		try {
			return restService.getClub(id);
		} catch (NotFoundException e) {
			return null;
		}
	}

	/**
	 * @see com.danshannon.strava.api.service.ClubServices#listAuthenticatedAthleteClubs()
	 */
	@Override
	public List<Club> listAuthenticatedAthleteClubs() throws UnauthorizedException {
		return Arrays.asList(restService.listAuthenticatedAthleteClubs());
	}

	/**
	 * @see com.danshannon.strava.api.service.ClubServices#listClubMembers(java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public List<Athlete> listClubMembers(Integer id, Paging pagingInstruction) {
		Strava.validatePagingArguments(pagingInstruction);
		try {
			return Arrays.asList(restService.listClubMembers(id, pagingInstruction.getPage(), pagingInstruction.getPageSize()));
		} catch (NotFoundException e) {
			return null;
		}
	}

	/**
	 * @see com.danshannon.strava.api.service.ClubServices#listRecentClubActivities(java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public List<Activity> listRecentClubActivities(Integer id, Paging pagingInstruction) {
		Strava.validatePagingArguments(pagingInstruction);
		try {
			return Arrays.asList(restService.listRecentClubActivities(id, pagingInstruction.getPage(), pagingInstruction.getPageSize()));
		} catch (NotFoundException e) {
			return null;
		}
	}

}
