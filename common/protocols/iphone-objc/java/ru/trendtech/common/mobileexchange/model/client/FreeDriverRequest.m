#import "FreeDriverRequest.h"

@implementation FreeDriverRequest

@synthesize missionInfo;

- (void) dealloc {
  [missionInfo release];
  [super dealloc];
}

@end
