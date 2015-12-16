#import "FindMissionResponse.h"

@implementation FindMissionResponse

@synthesize missionInfo;

- (void) dealloc {
  [missionInfo release];
  [super dealloc];
}

@end
