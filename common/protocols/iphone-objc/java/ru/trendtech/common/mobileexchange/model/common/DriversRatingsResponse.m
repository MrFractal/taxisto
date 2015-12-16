#import "DriversRatingsResponse.h"

@implementation DriversRatingsResponse

@synthesize ratings;

- (void) init {
  if (self = [super init]) {
    ratings = [[[NSMutableArray alloc] init] autorelease];
  }
  return self;
}

- (void) dealloc {
  [ratings release];
  [super dealloc];
}

@end
