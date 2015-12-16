#import "TripNotification.h"

@implementation TripNotification

@synthesize missionId;
@synthesize begin;

- (void) init {
  if (self = [super init]) {
    begin = NO;
  }
  return self;
}

@end
