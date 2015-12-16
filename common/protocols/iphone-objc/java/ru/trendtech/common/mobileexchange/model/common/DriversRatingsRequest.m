#import "DriversRatingsRequest.h"

@implementation DriversRatingsRequest

@synthesize driverId;

- (void) init {
  if (self = [super init]) {
    driverId = -1;
  }
  return self;
}

@end
