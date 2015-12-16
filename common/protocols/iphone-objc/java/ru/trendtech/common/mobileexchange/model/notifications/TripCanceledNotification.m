#import "TripCanceledNotification.h"

@implementation TripCanceledNotification

@synthesize missionId;
@synthesize reason;
@synthesize reasonMsg;

- (void) dealloc {
  [reasonMsg release];
  [super dealloc];
}

@end
