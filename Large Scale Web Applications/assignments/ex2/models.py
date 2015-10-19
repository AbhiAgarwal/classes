from django.db import models

# If you would like to use Django's Auth user model, uncomment the next line
# from django.contrib.auth.models import User

# Define your Sublet Application models here
# Example:
# class Foo(models.Model):
#     """Foo represents a real-world foo.
#    
#     If a Foo is active then it can be borked by any Zoot.
#     ... (other non-trivial comments)
#     """
#     bar = models.CharField(max_length=16, default="")
#     ... Your other fields go below

class Apartment(models.Model):
	name = models.CharField("Apartment name", max_length=100, blank=False, null=False)
	user = models.ForeignKey(User, blank=False, null=False)
	zip_code = models.CharField("ZIP code", max_length=5, blank=False, null=False)
	square_feet = models.IntegerField()
	bedrooms = models.IntegerField()
	floors = models.IntegerField()

	def __unicode__(self):
		return self.name

# List apartment/s for booking over a given period
# Dates: always in multiples of full days
class Listing(models.Model):
	name = models.CharField("Listing name", max_length=100, blank=False, null=False)
	user = models.ForeignKey(User, blank=False, null=False)
	apartment = models.ForeignKey(Apartment)
	start_date = models.DateTimeField("Start date and time")
	number_of_days = models.IntegerField()
	price_per_night = models.DecimalField("Cost (in dollars)", max_digits=10, decimal_places=2)
	is_booked = models.BooleanField(blank=False, null=False, default=False)

	def __unicode__(self):
		return self.name

	@classmethod
	def get_all_listings_time_period(cls, start_date, end_date):
		return cls.objects.filter(start_date__range=(start_date, end_date))

	@classmethod
	def get_all_listings_per_night(cls, price):
		return cls.objects.filter(price_per_night__lte=price)

class Booking(models.Model):
	user = models.ForeignKey(User, blank=False, null=False)
	listing = models.ForeignKey(Listing)

	def __unicode__(self):
		return "Booking: ", self.apartment.name

	@classmethod
	def create(current_booking):
		current_listing = current_booking.listing
		current_listing.is_booked = True
		current_listing.save()
		return current_booking
