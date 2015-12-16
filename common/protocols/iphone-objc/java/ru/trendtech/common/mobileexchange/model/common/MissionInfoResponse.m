#import "MissionInfoResponse.h"

@implementation MissionInfoResponse

@synthesize missionInfo;

- (void) dealloc {
  [missionInfo release];
  [super dealloc];
}

@end
