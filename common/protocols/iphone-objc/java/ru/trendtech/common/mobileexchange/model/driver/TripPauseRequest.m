#import "TripPauseRequest.h"

@implementation TripPauseRequest

@synthesize missionId;
@synthesize location;
@synthesize pauseBegin;
@synthesize pauseTime;

- (void) dealloc {
  [location release];
  [super dealloc];
}

@end
