#import "ItemLocation.h"

/**
 * Created by max on 09.02.14.
 */

@interface DriverLocationRequest : DriverRequest {
  ItemLocation * location;
}

@property(nonatomic, retain) ItemLocation * location;
- (void) setLocation:(ItemLocation *)location;
@end
