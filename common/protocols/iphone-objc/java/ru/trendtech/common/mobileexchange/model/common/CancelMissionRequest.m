#import "CancelMissionRequest.h"

@implementation CancelMissionRequest

@synthesize comment;
@synthesize initiatorId;
@synthesize missionId;
@synthesize reason;
@synthesize force;

- (void) dealloc {
  [comment release];
  [super dealloc];
}

@end
