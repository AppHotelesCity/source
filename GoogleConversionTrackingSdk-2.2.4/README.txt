The AdWords Conversion Tracking SDK is a lightweight SDK with two main purposes.
First, it allows you to measure the effectiveness of your AdWords app promotion
campaigns. You can tag the events you'd like to track in your app, then log in
to AdWords to see how many of these conversions are being driven by your
advertising. (Note that you can track the installs driven by your AdWords
advertising even without integrating this SDK--AdWords provides codeless install
tracking via an integration with Google Play.)

You can tag any event in your app to be tracked as a conversion--whenever users
perform the tagged action after interacting with an ad, the event is recorded
and can be viewed in your AdWords campaign reports. Details for setting up
tracked events are below.

Second, the SDK can also be used to tag user activity in your app, so you can
reach certain users with ads that re-engage them with your app later on. For
example, you might want to target app re-engagement ads to users who downloaded
your app but haven't used it recently.

The default integration of the AdWords conversion tracking SDK -- described
here:
https://developers.google.com/app-conversion-tracking/docs/android/conversion-tracking
-- collects information about app upgrades and app usage after the install. This
post-download usage data allows AdWords to optimize your advertising. This data
will also allow you to remarket to segments of your existing user base with app
re-engagement ads if you choose to set up remarketing later on in your AdWords
account.

You can also tag specific custom events in your app (ex. purchases or progress
in a game) for remarketing use, in order to reach users who have completed those
actions with re-engagement ads later on. Custom remarketing is described here:
https://developers.google.com/app-conversion-tracking/docs/android/custom-remarketing
