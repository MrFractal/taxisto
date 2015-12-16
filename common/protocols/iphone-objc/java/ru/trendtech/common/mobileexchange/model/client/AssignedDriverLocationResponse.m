#import "AssignedDriverLocationResponse.h"

@implementation AssignedDriverLocationResponse

@synthesize location;
@synthesize arrived;
@synthesize arrivalTimes;

- (void) init {
  if (self = [super init]) {
    arrived = NO;
    arrivalTimes = [[[NSMutableArray alloc] init] autorelease];
  }
  return self;
}

- (void) dealloc {
  [location release];
  [arrivalTimes release];
  [super dealloc];
}

@end
