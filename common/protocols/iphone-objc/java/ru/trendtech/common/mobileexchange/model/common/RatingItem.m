#import "RatingItem.h"

@implementation RatingItem

@synthesize driverId;
@synthesize firstName;
@synthesize driverRating;
@synthesize driverRatingPosition;
@synthesize driverRatingPositionsTotal;
@synthesize lastName;

- (id) init {
  if (self = [super init]) {
  }
  return self;
}

- (void) dealloc {
  [firstName release];
  [lastName release];
  [super dealloc];
}

@end
