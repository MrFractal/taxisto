#import "DriversAroundSubscription.h"

@implementation DriversAroundSubscription

@synthesize currentLocation;

- (void) dealloc {
  [currentLocation release];
  [super dealloc];
}

@end
