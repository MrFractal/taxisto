#import "DriversAroundRequest.h"

@implementation DriversAroundRequest

@synthesize currentLocation;

- (void) dealloc {
  [currentLocation release];
  [super dealloc];
}

@end
