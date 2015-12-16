#import "TripPauseNotification.h"

@implementation TripPauseNotification

@synthesize missionId;
@synthesize location;
@synthesize begin;

- (void) init {
  if (self = [super init]) {
    begin = NO;
  }
  return self;
}

- (void) dealloc {
  [location release];
  [super dealloc];
}

@end
