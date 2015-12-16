#import "DriversAroundResponse.h"

@implementation DriversAroundResponse

@synthesize locations;

- (void) init {
  if (self = [super init]) {
    locations = [[[NSMutableArray alloc] init] autorelease];
  }
  return self;
}

- (void) dealloc {
  [locations release];
  [super dealloc];
}

@end
