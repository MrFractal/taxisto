#import "DriverRequiredNotification.h"

@implementation DriverRequiredNotification

@synthesize missionInfo;

- (void) dealloc {
  [missionInfo release];
  [super dealloc];
}

@end
