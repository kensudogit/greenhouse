ALTER TABLE AccountProvider RENAME TO ServiceProvider;
UPDATE ServiceProvider SET implementation = 'com.springsource.greenhouse.connect.providers.TwitterServiceProvider' WHERE name = 'twitter';
UPDATE ServiceProvider SET implementation = 'com.springsource.greenhouse.connect.providers.FacebookServiceProvider' WHERE name = 'facebook';
UPDATE ServiceProvider SET implementation = 'com.springsource.greenhouse.connect.providers.LinkedInServiceProvider' WHERE name = 'linkedin';
UPDATE ServiceProvider SET implementation = 'com.springsource.greenhouse.connect.providers.TripItServiceProvider' WHERE name = 'tripit';