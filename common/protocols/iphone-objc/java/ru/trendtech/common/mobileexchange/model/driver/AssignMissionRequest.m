#import "AssignMissionRequest.h"

@implementation AssignMissionRequest

@synthesize missionId;
@synthesize arrivalTime;
@synthesize location;

- (void) dealloc {
  [location release];
  [super dealloc];
}

@end
